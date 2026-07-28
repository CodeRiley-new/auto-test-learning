Day 03 - 接口关联 + 数据驱动测试
学习日期：2026年7月17日

========================================
一、今日学习目标
========================================

[✓] 理解接口关联的概念和应用场景
[✓] 用 Postman 实现接口关联（获取 Token → 查询订单）
[✓] 用 RestAssured 实现接口关联（Java 代码）
[✓] 掌握 TestNG 的 dependsOnMethods 实现测试依赖
[✓] 掌握 @DataProvider 实现数据驱动测试
[✓] 学会配置代理解决网络超时问题


========================================
二、什么是接口关联？
========================================

通俗理解：一个接口的返回值作为另一个接口的入参。

    接口A（登录）→ 返回 Token
                        ↓
    接口B（查询订单）→ 需要 Token 作为参数
                        ↓
    接口C（修改信息）→ 需要 Token + 订单ID

真实场景：
    1. 登录接口返回 Token
    2. 后续所有接口都需要在请求头中携带这个 Token
    3. 测试脚本需要：提取 Token → 存储 → 传递给后续请求


========================================
三、测试环境
========================================

测试 API：https://restful-booker.herokuapp.com
认证接口：POST /auth
查询接口：GET /booking/{id}
代理端口：127.0.0.1:7890（根据你的代理软件调整）


========================================
四、接口关联核心代码（BookingAuthTest.java）
========================================

package com.yejunlong.apitests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

public class BookingAuthTest {

    private static String authToken;  // 存储 Token，供其他测试使用

    @BeforeClass
    public void setUp() {
        // 配置代理（重要：解决 Connection timed out）
        RestAssured.proxy("127.0.0.1", 7890);  // 把 7890 换成你的代理端口
        
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
    }

    /**
     * 测试用例1：获取 Token
     */
    @Test
    public void testGetToken() {
        String requestBody = "{\"username\":\"admin\",\"password\":\"password123\"}";

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
        .when()
                .post("/auth");

        response.prettyPrint();

        authToken = response.jsonPath().getString("token");
        System.out.println("✅ 提取的 Token: " + authToken);

        Assert.assertNotNull(authToken, "Token 不应为空");
        Assert.assertNotEquals(authToken, "", "Token 不应为空字符串");
    }

    /**
     * 测试用例2：使用 Token 查询订单（依赖 testGetToken）
     */
    @Test(dependsOnMethods = {"testGetToken"})
    public void testGetBookingWithToken() {
        given()
                .header("Cookie", "token=" + authToken)  // 关键：使用提取的 Token
        .when()
                .get("/booking/1")
        .then()
                .statusCode(200)
                .log().body();
    }
}


========================================
五、数据驱动测试核心代码（DataDrivenAuthTest.java）
========================================

package com.yejunlong.apitests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

public class DataDrivenAuthTest {

    @BeforeClass
    public void setUp() {
        RestAssured.proxy("127.0.0.1", 7890);
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
    }

    @DataProvider(name = "authData")
    public Object[][] authData() {
        return new Object[][]{
                {"admin", "password123", true},   // 正确凭证 → 返回 token
                {"admin", "wrong", false},        // 错误密码 → token 为 null
                {"", "password123", false},       // 空用户名
                {"admin", "", false}              // 空密码
        };
    }

    @Test(dataProvider = "authData")
    public void testAuthWithData(String username, String password, boolean shouldSucceed) {
        String requestBody = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
        .when()
                .post("/auth");

        response.then().statusCode(200);

        String token = response.jsonPath().getString("token");

        if (shouldSucceed) {
            Assert.assertNotNull(token, "正确凭证应返回 token");
            System.out.println("✅ 正确凭证，Token: " + token);
        } else {
            // Restful-Booker 对错误凭证返回 null（不是报错）
            Assert.assertNull(token, "错误凭证 token 应为 null");
            System.out.println("ℹ️ 错误凭证，Token 为 null");
        }
    }
}


========================================
六、pom.xml 依赖配置
========================================

<dependencies>
    <!-- RestAssured：接口测试核心框架 -->
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
        <version>5.3.2</version>
        <scope>test</scope>
    </dependency>

    <!-- TestNG：测试用例管理 -->
    <dependency>
        <groupId>org.testng</groupId>
        <artifactId>testng</artifactId>
        <version>7.8.0</version>
        <scope>test</scope>
    </dependency>

    <!-- Jackson：JSON 解析 -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.15.2</version>
        <scope>test</scope>
    </dependency>

    <!-- 日志（可选，消除 SLF4J 警告） -->
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.4.14</version>
        <scope>test</scope>
    </dependency>
</dependencies>


========================================
七、今日踩坑记录
========================================

【问题1】Connection timed out

报错信息：
    java.net.ConnectException: Connection timed out: connect

原因分析：
    需要代理才能访问外网 API（restful-booker.herokuapp.com）
    Java 代码默认不走系统代理

解决方案：
    在 @BeforeClass 的 setUp() 中添加：
    RestAssured.proxy("127.0.0.1", 7890);

    常见代理端口：
    - Clash: 7890
    - V2Ray: 10808 或 10809
    - Shadowsocks: 1080
    - Charles: 8888


【问题2】RestAssured 依赖找不到

原因分析：
    pom.xml 中没有添加 RestAssured 依赖

解决方案：
    在 pom.xml 中添加：
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
        <version>5.3.2</version>
        <scope>test</scope>
    </dependency>


========================================
八、TestNG 注解速查表
========================================

注解                          说明
@Test                         标记一个测试方法
@BeforeClass                  在类执行前执行一次
@AfterClass                   在类执行后执行一次
@BeforeMethod                 每个测试方法执行前执行
@AfterMethod                  每个测试方法执行后执行
@Test(dependsOnMethods)       依赖指定的测试方法先执行
@DataProvider(name)           提供测试数据
@Test(dataProvider)           使用数据提供者


========================================
九、运行结果
========================================

BookingAuthTest 运行结果：
===============================================
✅ 提取的 Token: e37d612c9a23133
✅ 用 Token 成功查询订单详情
===============================================
Total tests run: 2, Passes: 2, Failures: 0


DataDrivenAuthTest 运行结果：
===============================================
✅ 正确凭证，Token: e37d612c9a23133
ℹ️ 错误凭证，Token 为 null
ℹ️ 错误凭证，Token 为 null
ℹ️ 错误凭证，Token 为 null
===============================================
Total tests run: 4, Passes: 4, Failures: 0


========================================
十、核心知识点总结
========================================

技能                      说明
-------------------------------------------------
接口关联                  从一个接口的响应中提取数据，供后续接口使用
提取数据                  response.jsonPath().getString("token")
依赖管理                  @Test(dependsOnMethods = {"testA"})
数据驱动                  @DataProvider 提供多组测试数据
代理配置                  RestAssured.proxy("127.0.0.1", 端口)
JSONPath                  response.jsonPath().getInt("id")
断言                      Assert.assertNotNull() / Assert.assertNull()


========================================
十一、项目结构
========================================

auto-test-day03/
├── pom.xml                          # Maven 配置
├── README.md                        # 学习总结
└── src/
    └── test/
        └── java/
            └── com/
                └── yejunlong/
                    └── apitests/
                        ├── BookingAuthTest.java       # 接口关联测试
                        └── DataDrivenAuthTest.java    # 数据驱动测试


========================================
总结
========================================

第三天完成了接口关联和数据驱动测试，共 6 个测试用例全部通过。

掌握了：
    1. RestAssured 发送 POST/GET 请求
    2. 从响应中提取 Token
    3. 在后续请求中使用 Token（接口关联）
    4. @DataProvider 实现数据驱动测试
    5. TestNG 测试依赖管理
    6. 代理配置解决网络超时
