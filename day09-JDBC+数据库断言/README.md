# Day 09 - JDBC + 数据库断言

**学习日期：** 2026年7月25日


## 📌 今日目标

在接口自动化测试中引入数据库断言，验证 API 操作是否正确落库。


## ✅ 今日成果

| 测试用例 | 状态 |
|----------|------|
| testLogin | ✅ 通过 |
| testCreateBooking | ✅ 通过 |
| **合计** | **2/2 全部通过** |


## 🧱 今日新增内容

| 文件 | 作用 |
|------|------|
| DbUtils.java | 数据库工具类（连接、查询、执行SQL） |
| BookingDbTest.java | 带数据库断言的测试用例 |


## 📄 核心代码：DbUtils.java

```java
package com.yejunlong.utils;

import java.sql.*;

public class DbUtils {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/test_db?useSSL=false&serverTimezone=Asia/Shanghai";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";

    /**
     * 执行 INSERT / UPDATE / DELETE 语句
     */
    public static int executeSql(String sql) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            int rows = stmt.executeUpdate(sql);
            System.out.println("✅ 执行SQL成功，影响 " + rows + " 行");
            return rows;
        } catch (Exception e) {
            throw new RuntimeException("执行SQL失败：" + e.getMessage(), e);
        } finally {
            close(null, stmt, conn);
        }
    }

    /**
     * 查询单个字符串值
     */
    public static String queryString(String sql) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getString(1);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("查询失败：" + e.getMessage(), e);
        } finally {
            close(rs, stmt, conn);
        }
    }

    private static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private static void close(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
🔁 数据流转链路
text
API 创建订单
    ↓ 返回 bookingid: 7208
往本地数据库插入同样数据
    ↓ INSERT INTO booking (id, firstname, ...) VALUES (7208, '张三', ...)
从数据库查询 firstname
    ↓ SELECT firstname FROM booking WHERE id = 7208
查询结果："张三"
    ↓ Assert.assertEquals(dbFirstname, "张三")
✅ 数据库断言通过
💻 测试用例代码
java
@Test(dependsOnMethods = "testLogin")
public void testCreateBooking() {
    // 1. 构造请求数据
    BookingRequest request = new BookingRequest();
    request.setFirstname("张三");
    request.setLastname("测试");
    request.setTotalprice(150);
    request.setDepositpaid(true);
    BookingRequest.BookingDates dates = new BookingRequest.BookingDates();
    dates.setCheckin("2026-07-25");
    dates.setCheckout("2026-07-27");
    request.setBookingdates(dates);
    request.setAdditionalneeds("早餐");

    // 2. 发送 API 请求创建订单
    Response response = BookingApi.createBooking(request);
    AssertUtils.assertSuccess(response);

    // 3. 提取 bookingId
    BookingResponse bookingResponse = JsonUtils.fromJson(response.getBody().asString(), BookingResponse.class);
    bookingId = bookingResponse.getBookingid();
    System.out.println("创建订单成功 bookingId: " + bookingId);

    // 4. 往本地数据库插入同样的数据
    String insertSql = "INSERT INTO booking (id, firstname, lastname, totalprice, depositpaid, checkin, checkout) " +
            "VALUES (" + bookingId + ", '张三', '测试', 150, 1, '2026-07-25', '2026-07-27')";
    DbUtils.executeSql(insertSql);

    // 5. 从数据库查询验证
    String sql = "SELECT firstname FROM booking WHERE id = " + bookingId;
    String dbFirstname = DbUtils.queryString(sql);
    System.out.println("数据库查询结果：" + dbFirstname);

    // 6. 断言
    Assert.assertEquals(dbFirstname, "张三", "数据库中的数据与API请求不一致");
    System.out.println("✅ 数据库断言成功");
}
🐛 今日踩坑记录
问题1：SQL 字段名写错
错误现象：

text
java.sql.SQLSyntaxErrorException: Unknown column 'bookingid' in 'where clause'
原因： 表中字段名是 id，但 SQL 里写成了 bookingid。

解决： 用 DESCRIBE booking; 查看表结构，确认字段名后修正 SQL。

问题2：查错字段导致断言失败
错误现象：

text
数据库查询结果：7208
预期:张三
实际:7208
原因： SQL 写的是 SELECT id，却用 "张三" 来断言，字段不匹配。

解决： SELECT 查什么字段，就用什么字段的值来断言。

问题3：SQL 拼接缺少空格
错误现象：

text
SELECT id FROM booking WHERE firstname = '张三'7208
原因： '张三' 后面直接拼了 7208，缺少 AND 或空格。

解决： 改用 WHERE id = + bookingId 直接查询。

📊 JDBC 操作五步法
text
第1步：加载驱动      → Class.forName("com.mysql.cj.jdbc.Driver")
第2步：建立连接      → DriverManager.getConnection(url, user, password)
第3步：创建 Statement → conn.createStatement()
第4步：执行 SQL      → stmt.executeUpdate(sql) 或 stmt.executeQuery(sql)
第5步：处理结果      → ResultSet 遍历 / 关闭资源
📁 今日项目结构
text
auto-test-day09/
├── pom.xml
└── src/
    └── test/
        └── java/
            └── com/
                └── yejunlong/
                    ├── config/
                    │   └── ApiConfig.java
                    ├── client/
                    │   └── ApiClient.java
                    ├── utils/
                    │   ├── AssertUtils.java
                    │   ├── JsonUtils.java
                    │   └── DbUtils.java          ← 新增
                    ├── model/
                    │   ├── AuthRequest.java
                    │   ├── AuthResponse.java
                    │   ├── BookingRequest.java
                    │   └── BookingResponse.java
                    ├── api/
                    │   ├── AuthApi.java
                    │   └── BookingApi.java
                    └── tests/
                        ├── BookingTest.java
                        └── BookingDbTest.java    ← 新增
💡 核心知识点总结
知识点	说明
JDBC	Java 连接数据库的标准 API
数据库断言	从数据库查询数据，验证 API 操作是否正确落库
双重验证	API 响应验证 + 数据库验证，确保数据一致性
Statement	执行 SQL 语句的对象
ResultSet	存储查询结果的对象
资源关闭	使用后必须关闭 Connection、Statement、ResultSet
📌 今日总结
今天完成了 Day 9 的任务：在测试中集成 JDBC，实现数据库断言。

关键收获：

掌握了 JDBC 连接 MySQL 的完整流程

学会了在测试中执行 SQL 插入和查询

理解了 API 响应验证 + 数据库验证的双重保障机制
