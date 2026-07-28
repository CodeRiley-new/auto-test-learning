Day 04 - 接口测试框架封装
学习日期：2026年7月18日

========================================
一、今日学习目标
========================================

[✓] 封装 ApiClient（统一请求管理）
[✓] 封装 AssertUtils（统一断言管理）
[✓] 用封装后的代码重写测试用例
[✓] 3个测试用例全部通过


========================================
二、为什么要封装？
========================================

封装前的问题：
    ❌ 每个请求都要写 contentType、log、代理配置
    ❌ 断言代码分散在各处，错误信息不统一
    ❌ 改一个配置（如代理端口）要改多个文件

封装后的效果：
    ✅ ApiClient.post("/auth", body) 一行发送请求
    ✅ AssertUtils.assertSuccess(response) 一行断言
    ✅ 代理配置写在静态块中，全局生效


========================================
三、封装的核心代码
========================================

【ApiClient.java - 统一请求客户端】

public class ApiClient {
    static {
        RestAssured.proxy("127.0.0.1", 7897);
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        RestAssured.useRelaxedHTTPSValidation();
    }

    public static Response post(String path, String body) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .log().all()
        .when()
                .post(path)
        .then()
                .log().all()
                .extract().response();
    }

    public static Response getWithToken(String path, String token) {
        return given()
                .header("Cookie", "token=" + token)
                .log().all()
        .when()
                .get(path)
        .then()
                .log().all()
                .extract().response();
    }
}


【AssertUtils.java - 统一断言工具】

public class AssertUtils {
    public static void assertSuccess(Response response) {
        Assert.assertEquals(response.getStatusCode(), 200,
                "请求失败，状态码：" + response.getStatusCode());
    }

    public static void assertTokenValid(String token) {
        Assert.assertNotNull(token, "Token 不应为 null");
        Assert.assertNotEquals(token, "", "Token 不应为空字符串");
    }

    public static void assertFieldExists(Response response, String jsonPath) {
        Object value = response.jsonPath().get(jsonPath);
        Assert.assertNotNull(value, "字段 '" + jsonPath + "' 不存在");
    }
}


【BookingAuthTest.java - 重写后的测试用例】

public class BookingAuthTest {
    private static String authToken;

    @Test
    public void testGetToken() {
        String body = "{\"username\":\"admin\",\"password\":\"password123\"}";
        Response response = ApiClient.post("/auth", body);
        
        authToken = response.jsonPath().getString("token");
        System.out.println("authToken = " + authToken);
        
        AssertUtils.assertSuccess(response);
        AssertUtils.assertTokenValid(authToken);
    }

    @Test(dependsOnMethods = {"testGetToken"})
    public void testGetBookingWithToken() {
        Response response = ApiClient.getWithToken("/booking/1", authToken);
        AssertUtils.assertSuccess(response);
        AssertUtils.assertFieldExists(response, "firstname");
        System.out.println("成功查询订单详细");
    }

    @Test
    public void testGetTokenWithWrongPassword() {
        String body = "{\"username\":\"admin\",\"password\":\"wrong\"}";
        Response response = ApiClient.post("/auth", body);
        
        String token = response.jsonPath().getString("token");
        AssertUtils.assertSuccess(response);
        Assert.assertNull(token, "错误密码应返回 null");
        System.out.println("错误密码测试通过，Token 为null");
    }
}


========================================
四、运行结果
========================================

✅ 提取的 Token：1669741919bfec3
✅ 错误密码测试通过，Token 为 null
✅ 成功查询订单详情

===============================================
Total tests run: 3, Passes: 3, Failures: 0, Skips: 0
===============================================


========================================
五、封装前后对比
========================================

对比项              封装前                    封装后
-------------------------------------------
发送 POST 请求      5-6 行重复代码            ApiClient.post() 一行
添加 Token          手动加 header             自动在 getWithToken 中处理
代理配置            每个类都要写              写在静态块中，全局生效
断言代码            分散在各处                统一在 AssertUtils 中
维护成本            改一处要改多处            只需改封装类


========================================
六、核心知识点
========================================

知识点              说明
-------------------------------------------
封装                把重复代码抽取到公共方法中
静态代码块          static { } 在类加载时执行一次，适合做初始化
工具类设计          只包含静态方法的类，如 ApiClient、AssertUtils
单一职责原则        每个类只做一件事
代码复用            一个方法替代多行重复代码


========================================
七、项目结构
========================================

auto-test-day04/
├── pom.xml
├── README.md
└── src/
    └── test/
        └── java/
            └── com/
                └── yejunlong/
                    ├── client/
                    │   └── ApiClient.java
                    ├── utils/
                    │   └── AssertUtils.java
                    └── tests/
                        └── BookingAuthTest.java


========================================
总结
========================================

第四天完成了接口测试框架封装，3 个测试用例全部通过。
掌握了代码封装的核心思想，为后续框架搭建打下基础。
