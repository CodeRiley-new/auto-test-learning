Day 08 - 框架独立搭建实战

学习日期：2026年7月24日

一、今日目标

从零开始，不参考 Day 7 代码，独立搭建一个完整的接口自动化测试框架，并跑通 3 个测试用例。

二、今日成果

测试用例	状态
testLogin	通过
testCreateBooking	通过
testGetBooking	通过
合计	3/3 全部通过
三、框架搭建完整流程（10步法）

步骤	做什么	核心内容
第1步	创建 Maven 项目	GroupId: com.yejunlong，ArtifactId: auto-test-day08
第2步	创建包结构	client、utils、model、api、config、tests
第3步	配置 pom.xml	添加 rest-assured、jackson-databind、testng 依赖
第4步	写 ApiConfig.java	BASE_URL、PROXY_HOST、PROXY_PORT、账号密码
第5步	写 ApiClient.java	静态块配置代理和 BaseURI，post()，getWithToken()
第6步	写 AssertUtils.java	assertSuccess()、assertTokenValid()、assertFieldExist()
第7步	写 JsonUtils.java	toJson()、fromJson()
第8步	写 Model 层	AuthRequest、AuthResponse、BookingRequest、BookingResponse
第9步	写 API 层	AuthApi（login）、BookingApi（createBooking、getBooking）
第10步	写 Tests 层	testLogin、testCreateBooking、testGetBooking
四、各层文件职责说明

ApiConfig.java（配置管理）

作用：集中管理所有配置（URL、代理、账号），改配置不用改代码。
内容：BASE_URL、PROXY_HOST、PROXY_PORT、TEST_USERNAME、TEST_PASSWORD。

ApiClient.java（请求客户端）

作用：封装 HTTP 请求的公共部分（代理、Content-Type、日志），测试用例不用重复写。
核心：静态块配置代理和 BaseURI，post() 方法，getWithToken() 方法。

AssertUtils.java（断言工具）

作用：封装常用断言，统一错误信息格式。
核心方法：assertSuccess()、assertTokenValid()、assertFieldExist()、assertStatusCode()。

JsonUtils.java（JSON 工具）

作用：Java 对象和 JSON 字符串互相转换。
核心方法：toJson()、fromJson()。

Model 层（数据模型）

AuthRequest：username、password。
AuthResponse：token。
BookingRequest：firstname、lastname、totalprice、depositpaid、bookingdates（内部类）、additionalneeds。
BookingResponse：bookingid、booking。

API 层（接口定义）

AuthApi：login() → POST /auth。
BookingApi：createBooking() → POST /booking，getBooking() → GET /booking/{id}。

Tests 层（测试用例）

testLogin：登录 → 获取 token → 存入类变量 authToken。
testCreateBooking：创建订单 → 获取 bookingId → 存入类变量 bookingId（依赖 testLogin）。
testGetBooking：查询订单 → 断言 firstname = "张三"（依赖 testLogin 和 testCreateBooking）。

五、数据流转链路

testLogin
↓ 获取 token，存入类变量 authToken
testCreateBooking（依赖 testLogin）
↓ 创建订单，存入类变量 bookingId
testGetBooking（依赖 testLogin + testCreateBooking）
↓ 用 authToken + bookingId 查询订单
↓ 断言 firstname = "张三"
全部通过

六、今日踩坑记录

问题1：字段名不匹配导致 JSON 解析失败

错误信息：Unrecognized field "bookingid" (class BookingResponse), not marked as ignorable

原因：API 返回的是 "bookingid"（全小写），但 Java 类里写的是 "bookingId"（驼峰），Jackson 找不到对应的字段。

解决：把 Java 字段名改成和 JSON 完全一致（bookingid），或使用 @JsonProperty 注解映射。

问题2：bookingId 没存成类变量

原因：在 testCreateBooking 方法里提取了 bookingId，但只存成了局部变量，testGetBooking 方法里拿不到。

解决：在类级别声明 private static int bookingId，在 testCreateBooking 中赋值。

问题3：查询时用了硬编码的 ID

原因：testGetBooking 里写的是 BookingApi.getBooking(authToken, 1)，用了固定值 1。

解决：改成 BookingApi.getBooking(authToken, bookingId)，使用真正创建的订单 ID。

七、分层架构总结

层级	文件	职责
Config 层	ApiConfig.java	存放所有配置（URL、代理、账号）
Client 层	ApiClient.java	封装 HTTP 请求（代理、Header、日志）
Utils 层	AssertUtils.java、JsonUtils.java	通用工具（断言、JSON 转换）
Model 层	AuthRequest、AuthResponse、BookingRequest、BookingResponse	数据模型（POJO）
API 层	AuthApi.java、BookingApi.java	接口定义（路径、调用方式）
Tests 层	BookingTest.java	测试用例（登录、创建、查询）
八、核心知识点总结

知识点	说明
分层架构	Config → Client → Utils → Model → API → Tests，各层各司其职
封装	把重复代码抽到公共方法，减少冗余
POJO	用 Java 对象表示 JSON 数据，字段名必须和 JSON 一致
dependsOnMethods	控制测试用例执行顺序，确保依赖的数据先准备好
类变量传参	用 static 变量在测试方法之间传递数据（authToken、bookingId）
Jackson	ObjectMapper 负责对象和 JSON 的互转
静态代码块	static { } 在类加载时执行一次，适合做全局初始化
九、验收标准

3 个测试用例全部通过

不复制 Day 7 代码，独立完成

理解每一层的作用和调用关系

能说出数据流转的完整链路

十、项目结构

auto-test-day08/
├── pom.xml
└── src/
└── test/
└── java/
└── com/
└── yejunlong/
├── config/
│ └── ApiConfig.java
├── client/
│ └── ApiClient.java
├── utils/
│ ├── AssertUtils.java
│ └── JsonUtils.java
├── model/
│ ├── AuthRequest.java
│ ├── AuthResponse.java
│ ├── BookingRequest.java
│ └── BookingResponse.java
├── api/
│ ├── AuthApi.java
│ └── BookingApi.java
└── tests/
└── BookingTest.java

十一、今日总结

今天完成了 Day 8 的任务：独立搭建接口自动化测试框架。

存在的问题：

还不能完整脱稿敲出所有代码

部分知识点记忆不牢固

对框架整体结构的串联还不够清晰

收获：

对分层架构的理解更加深入

熟悉了框架搭建的完整流程（10 步法）

掌握了数据流转的核心链路

能够独立排查字段名不匹配、类变量传递等常见问题

