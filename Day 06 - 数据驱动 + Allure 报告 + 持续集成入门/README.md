Day 06 - 数据驱动 + Allure 报告 + 持续集成入门

学习日期：2026年7月21日


========================================
一、今日学习目标
========================================

[✓] 用 JSON/YAML 文件管理测试数据
[✓] 实现数据驱动测试（DataProvider + 外部文件）
[✓] 集成 Allure 测试报告
[✓] 使用 Lombok 简化 POJO 代码
[✓] Jenkins 持续集成入门


========================================
二、今日新增的核心功能
========================================

1. 数据驱动测试进阶
   - 从 JSON 文件读取测试数据
   - 从 YAML 文件读取测试数据
   - DataLoader 统一数据加载

2. Allure 测试报告
   - 美观的可视化报告
   - 测试分类（@Feature / @Story）
   - 测试描述（@Description）

3. Lombok 简化代码
   - @Data：自动生成 Getter/Setter
   - @NoArgsConstructor：无参构造
   - @AllArgsConstructor：全参构造


========================================
三、数据驱动流程图
========================================

  testdata.yml / testdata.json
         ↓
  DataLoader.loadYaml() / loadJson()
         ↓
  List<Map<String, Object>>
         ↓
  @DataProvider
         ↓
  testCreateBookingWithData(Map<String, Object> data)
         ↓
  构造 BookingRequest 对象
         ↓
  调用 BookingApi.createBooking()
         ↓
  断言验证


========================================
四、Allure 报告常用注解
========================================

  注解                    作用
  ──────────────────────────────────────────
  @Feature("模块名")       对测试进行模块分类
  @Story("功能点")         对测试进行功能点分类
  @Description("描述")     为测试添加详细描述


========================================
五、Lombok 常用注解
========================================

  注解                    作用
  ──────────────────────────────────────────
  @Data                   生成 Getter/Setter/toString
  @NoArgsConstructor      生成无参构造方法
  @AllArgsConstructor     生成全参构造方法
  @Builder                建造者模式


========================================
六、今日项目结构
========================================

  auto-test-day06/
  ├── pom.xml
  ├── testng.xml
  ├── src/
  │   └── test/
  │       ├── java/
  │       │   └── com/
  │       │       └── yejunlong/
  │       │           ├── config/
  │       │           │   └── ApiConfig.java
  │       │           ├── client/
  │       │           │   └── ApiClient.java
  │       │           ├── utils/
  │       │           │   ├── AssertUtils.java
  │       │           │   ├── JsonUtils.java
  │       │           │   └── DataLoader.java      ← 新增
  │       │           ├── model/
  │       │           │   ├── AuthRequest.java
  │       │           │   ├── AuthResponse.java
  │       │           │   ├── BookingRequest.java
  │       │           │   └── BookingResponse.java
  │       │           ├── api/
  │       │           │   ├── AuthApi.java
  │       │           │   └── BookingApi.java
  │       │           └── tests/
  │       │               └── BookingDataDrivenTest.java  ← 新增
  │       └── resources/
  │           ├── testdata.yml      ← 新增
  │           └── testdata.json     ← 新增
  └── Day06_学习总结.txt


========================================
七、运行验证
========================================

  # 运行所有测试
  mvn clean test

  # 查看 Allure 报告
  mvn allure:serve

  # 预期结果
  Total tests run: 8, Passes: 8, Failures: 0, Skips: 0


========================================
八、今日踩坑记录
========================================

  【问题1】找不到 aspectjweaver 依赖
  解决：移除 aspectjweaver 的 argLine 配置，改用 Allure 监听器

  【问题2】无法解析符号 'YamlFactory'
  解决：刷新 Maven，重新下载 jackson-dataformat-yaml

  【问题3】文件不存在：testdata.yaml
  解决：文件名是 testdata.yml，不是 testdata.yaml


========================================
总结
========================================

Day 6 完成了数据驱动测试的进阶实现：
  ✅ 外部文件（JSON/YAML）管理测试数据
  ✅ DataLoader 统一加载
  ✅ Allure 专业测试报告
  ✅ Lombok 简化 POJO 代码
  ✅ 数据与代码完全分离

6 天累计完成 6 个模块，测试用例从 3 个增长到 8 个。
