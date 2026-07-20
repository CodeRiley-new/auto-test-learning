Day 05 - 接口测试框架深度封装 + 数据分离

学习日期：2026年7月20日


========================================
一、今日学习目标
========================================

[✓] 学习 POJO 数据模型（用对象代替 JSON 字符串）
[✓] 封装 API 接口层（集中管理所有接口）
[✓] 测试数据与代码分离（配置文件）
[✓] 掌握 Jackson 实现对象与 JSON 互转
[✓] 完成一个完整的业务流测试（登录 → 创建订单 → 查询订单）


========================================
二、为什么要进一步封装？
========================================

Day 3-4 代码存在的问题：

  ❌ JSON 字符串硬编码在代码里
     String body = "{\"username\":\"admin\",\"password\":\"password123\"}";
     字段名写错了编译器不报错，运行时才发现

  ❌ 接口路径散落在各个测试类中
     ApiClient.post("/auth", body);
     ApiClient.getWithToken("/booking/1", token);
     改路径要改多个地方，容易漏改

  ❌ 测试数据混在代码里
     String username = "admin";
     改数据要改代码，重新编译


Day 5 封装后的效果：

  ✅ 用 Java 对象代替 JSON 字符串
     AuthRequest request = new AuthRequest("admin", "password123");
     Response response = AuthApi.login(request);
     // 字段写错 IDE 会直接报红提示

  ✅ 接口路径统一管理在 Api 类中
     AuthApi.LOGIN_PATH = "/auth"
     外部不用关心路径，改一处全局生效

  ✅ 数据从配置文件读取
     String username = ApiConfig.DEFAULT_USERNAME;


========================================
三、今日新增的层级结构
========================================

Day 5 在 Day 4 的基础上，新增了 3 个层级：

  ┌─────────────────────────────────────────────────────┐
  │  Model 层（POJO 数据模型）      ← 今日新增        │
  │  作用：用 Java 对象表示请求/响应数据               │
  │  文件：AuthRequest, AuthResponse,                  │
  │        BookingRequest, BookingResponse             │
  ├─────────────────────────────────────────────────────┤
  │  API 层（接口定义）             ← 今日新增        │
  │  作用：集中管理所有接口路径和调用方式               │
  │  文件：AuthApi, BookingApi                        │
  ├─────────────────────────────────────────────────────┤
  │  Config 层（配置管理）          ← 今日新增        │
  │  作用：集中管理 URL、账号、代理等配置              │
  │  文件：ApiConfig                                  │
  ├─────────────────────────────────────────────────────┤
  │  Utils 层（工具类）            ← Day 4 已有       │
  │  作用：封装通用工具方法                           │
  │  文件：AssertUtils, JsonUtils（新增）              │
  ├─────────────────────────────────────────────────────┤
  │  Client 层（请求客户端）       ← Day 4 已有       │
  │  作用：封装 HTTP 请求公共部分                     │
  │  文件：ApiClient                                  │
  ├─────────────────────────────────────────────────────┤
  │  Tests 层（测试用例）                            │
  │  作用：编写具体测试用例                           │
  │  文件：BookingFlowTest                            │
  └─────────────────────────────────────────────────────┘


========================================
四、分层职责详解
========================================

【1. Model 层 - POJO 数据模型】

  作用：用 Java 对象来表示 JSON 数据结构

  示例：
  ┌─────────────────────────────────────────────┐
  │ JSON：                                      │
  │ {"username":"admin","password":"password123"}│
  └─────────────────────────────────────────────┘
                    ↓ 映射为
  ┌─────────────────────────────────────────────┐
  │ Java 对象：                                 │
  │ AuthRequest request = new AuthRequest();    │
  │ request.setUsername("admin");               │
  │ request.setPassword("password123");         │
  └─────────────────────────────────────────────┘

  优点：
  • 字段名写错 IDE 会报错（编译时检查）
  • 有类型安全（int 不会误传成 String）
  • 代码提示友好（输入 request. 会弹出所有字段）


【2. API 层 - 接口定义】

  作用：集中管理所有接口的路径和调用方式

  示例：
  ┌─────────────────────────────────────────────┐
  │ public class AuthApi {                     │
  │     public static final String LOGIN_PATH   │
  │         = "/auth";                         │
  │                                             │
  │     public static Response login(           │
  │         AuthRequest request) {             │
  │         String json = JsonUtils.toJson(    │
  │             request);                      │
  │         return ApiClient.post(LOGIN_PATH,  │
  │             json);                         │
  │     }                                      │
  │ }                                          │
  └─────────────────────────────────────────────┘

  优点：
  • 接口路径集中管理，改一处全局生效
  • 调用方不需要关心路径和序列化细节
  • 测试代码更简洁：AuthApi.login(request)


【3. Config 层 - 配置管理】

  作用：集中管理所有配置项

  ┌─────────────────────────────────────────────┐
  │ public class ApiConfig {                   │
  │     public static final String ENV         │
  │         = "test";                         │
  │     public static final String PROXY_HOST  │
  │         = "127.0.0.1";                    │
  │     public static final int PROXY_PORT     │
  │         = 7897;                           │
  │     public static final String BASE_URL    │
  │         = "https://restful-booker...";    │
  │ }                                          │
  └─────────────────────────────────────────────┘

  优点：
  • 改配置不用改代码
  • 方便切换环境（test/prod）


【4. Utils 层 - 工具类】

  JsonUtils 的作用：
  ┌─────────────────────────────────────────────┐
  │ 对象 → JSON：JsonUtils.toJson(object)      │
  │ JSON → 对象：JsonUtils.fromJson(json, 类)  │
  └─────────────────────────────────────────────┘

  为什么需要 Jackson？
  • RestAssured 的 .body() 方法需要传入 JSON 字符串
  • 我们的测试数据是 Java 对象
  • 所以需要在中间做一次转换


========================================
五、POJO 类的设计规范
========================================

一个标准的 POJO 类包含：

  1. 私有字段（private）
     └── 字段名与 JSON 的 key 完全一致

  2. 无参构造方法
     └── Jackson 反序列化时需要

  3. 有参构造方法（可选）
     └── 方便快速创建对象

  4. Getter 和 Setter
     └── Jackson 通过它们读写字段

  5. 内部静态类（处理嵌套 JSON）
     └── 如 BookingRequest.BookingDates


嵌套 JSON 的处理示例：

  原始 JSON：
  ┌─────────────────────────────────────────────┐
  │ {                                           │
  │   "firstname": "张三",                      │
  │   "bookingdates": {                         │
  │       "checkin": "2026-07-20",             │
  │       "checkout": "2026-07-22"             │
  │   }                                        │
  │ }                                           │
  └─────────────────────────────────────────────┘

  映射为 Java 类：
  ┌─────────────────────────────────────────────┐
  │ public class BookingRequest {              │
  │     private String firstname;              │
  │     private BookingDates bookingdates;     │
  │                                             │
  │     public static class BookingDates {     │
  │         private String checkin;            │
  │         private String checkout;           │
  │         // Getter/Setter                   │
  │     }                                      │
  │     // Getter/Setter                       │
  │ }                                          │
  └─────────────────────────────────────────────┘


========================================
六、今日完成的测试用例
========================================

  测试类：BookingFlowTest

  ┌─────────────────────────────────────────────┐
  │ 测试用例1：testLogin                       │
  │ 步骤：调用 AuthApi.login()                 │
  │ 验证：状态码 200、Token 不为空             │
  │ 状态：✅ 通过                              │
  ├─────────────────────────────────────────────┤
  │ 测试用例2：testCreateBooking               │
  │ 依赖：testLogin 必须先执行                  │
  │ 步骤：用 POJO 构造订单数据，调用           │
  │       BookingApi.createBooking()           │
  │ 验证：状态码 200、订单 ID > 0、姓名匹配    │
  │ 状态：✅ 通过                              │
  ├─────────────────────────────────────────────┤
  │ 测试用例3：testGetBooking                  │
  │ 依赖：testLogin + testCreateBooking        │
  │ 步骤：用 Token + 订单ID 查询订单           │
  │ 验证：状态码 200、响应体包含 firstname     │
  │ 状态：✅ 通过                              │
  ├─────────────────────────────────────────────┤
  │ 测试用例4：testFullFlow                    │
  │ 依赖：前三个用例全部通过                    │
  │ 验证：完整流程跑通                         │
  │ 状态：✅ 通过                              │
  └─────────────────────────────────────────────┘


========================================
七、运行结果
========================================

  ✅ 登录成功，Token：xxx
  ✅ 创建订单成功，订单 ID：123
  ✅ 查询订单成功，订单 ID：123
  ✅ 完整流程测试通过：登录 → 创建订单 → 查询订单

  ===============================================
  Default Suite
  Total tests run: 4, Passes: 4, Failures: 0, Skips: 0
  ===============================================


========================================
八、封装层级演进（Day 3 → Day 5）
========================================

  ┌──────────────┬─────────────────┬─────────────────┐
  │   对比项     │   Day 3          │   Day 5          │
  ├──────────────┼─────────────────┼─────────────────┤
  │ 请求发送     │ 直接写 given()  │ AuthApi.login() │
  │ 数据表示     │ JSON 字符串     │ POJO 对象       │
  │ 接口路径     │ 散落在测试类中  │ 常量集中管理    │
  │ 配置管理     │ 硬编码          │ ApiConfig 统一  │
  │ 断言         │ 写在测试类中    │ AssertUtils     │
  │ 代码复用     │ 低              │ 高              │
  │ 维护成本     │ 高              │ 低              │
  └──────────────┴─────────────────┴─────────────────┘


========================================
九、今日核心知识点总结
========================================

  知识点              说明
  ──────────────────────────────────────────────
  POJO                Plain Old Java Object
                      用 Java 对象表示数据

  Jackson             对象 ↔ JSON 互转的框架

  内部静态类          用于映射 JSON 中的嵌套对象

  分层架构            Model → Api → Client → Utils
                      → Config → Tests

  配置分离            把 URL、账号等抽到 Config 类

  接口层封装          把 API 路径和调用集中管理


========================================
十、项目结构
========================================

  auto-test-day05/
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
                      │   └── JsonUtils.java
                      ├── model/
                      │   ├── AuthRequest.java
                      │   ├── AuthResponse.java
                      │   ├── BookingRequest.java
                      │   └── BookingResponse.java
                      ├── api/
                      │   ├── AuthApi.java
                      │   └── BookingApi.java
                      └── tests/
                          └── BookingFlowTest.java


========================================
总结
========================================

Day 5 完成了接口测试框架的深度封装：

  1. Model 层：用 POJO 对象代替 JSON 字符串
  2. API 层：集中管理接口路径和调用方式
  3. Config 层：配置与代码分离
  4. Utils 层：JsonUtils 实现对象 ↔ JSON 互转
  5. 完整业务流测试：登录 → 创建订单 → 查询订单

  从 Day 3 的"能跑通"，到 Day 4 的"能复用"，再到 Day 5 的"能分层"，
  你已经搭建了一个企业级接口自动化测试框架的雏形。

  下一步可以继续优化：
  - 接入 Allure 测试报告
  - 接入 Jenkins 持续集成
  - 接入 Excel/YAML 数据驱动
