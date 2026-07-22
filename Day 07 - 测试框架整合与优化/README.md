Day 07 - 测试框架整合与优化

学习日期：2026年7月22日


========================================
一、今日学习目标
========================================

[✓] 完善测试基类 BaseTest
[✓] 优化 testng.xml 配置
[✓] 解决测试执行顺序问题
[✓] 完成冒烟测试 + 数据驱动测试整合
[✓] 11 个测试用例全部通过


========================================
二、今日核心工作
========================================

1. testng.xml 配置优化
   - 添加 preserve-order="true" 控制执行顺序
   - 冒烟测试先执行，数据驱动测试后执行
   - 配置 Allure 监听器

2. BaseTest 基类完善
   - 添加 @BeforeSuite 打印环境信息
   - 添加 attachText / attachJson 方法（Allure 附件）
   - 统一初始化和清理

3. 测试执行顺序问题解决
   - 问题：单独运行通过，testng.xml 运行失败
   - 原因：测试类之间执行顺序不可控，导致数据污染
   - 解决：preserve-order="true" 强制顺序执行


========================================
三、最终 testng.xml 配置
========================================

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="接口自动化测试套件" verbose="1" parallel="false">

    <listeners>
        <listener class-name="io.qameta.allure.testng.AllureTestNg"/>
    </listeners>

    <!-- 冒烟测试：先执行 -->
    <test name="冒烟测试" preserve-order="true">
        <classes>
            <class name="com.yejunlong.tests.BookingSmokeTest"/>
        </classes>
    </test>

    <!-- 数据驱动测试：后执行 -->
    <test name="数据驱动测试" preserve-order="true">
        <classes>
            <class name="com.yejunlong.tests.BookingDataDrivenTest"/>
        </classes>
    </test>

</suite>


========================================
四、今日完成的测试用例
========================================

【冒烟测试 BookingSmokeTest】- 4 个用例
┌─────────────────────────────────────────────┐
│ testLoginSmoke        → 登录功能验证       │
│ testCreateBookingSmoke → 创建订单验证      │
│ testGetBookingSmoke   → 查询订单验证       │
│ testDeleteBookingSmoke → 删除订单验证      │
└─────────────────────────────────────────────┘

【数据驱动测试 BookingDataDrivenTest】- 7 个用例
┌─────────────────────────────────────────────┐
│ testCreateBookingWithJsonData  → JSON 数据 │
│   ├── 张三 / 150                           │
│   ├── 李四 / 200                           │
│   ├── 王五 / 180                           │
│   └── 赵六 / 220                           │
│ testCreateBookingWithYamlData  → YAML 数据 │
│   ├── 张三 / 150                           │
│   ├── 李四 / 200                           │
│   └── 王五 / 180                           │
│ testDataCount          → 数据条数验证      │
└─────────────────────────────────────────────┘

合计：11 个用例，全部通过 ✅


========================================
五、今日踩坑记录
========================================

【问题】单独运行测试类通过，testng.xml 运行失败

原因分析：
  - testng.xml 中多个 <test> 标签默认按顺序执行
  - 但 <classes> 内部执行顺序不可控
  - 冒烟测试删除数据后，可能影响数据驱动测试

解决方案：
  - 在 <test> 标签中添加 preserve-order="true"
  - 明确指定测试执行顺序
  - 确保每个测试类独立（自己创建、自己清理）


========================================
六、核心知识点总结
========================================

知识点                    说明
─────────────────────────────────────────────
preserve-order="true"    强制 testng.xml 按声明顺序执行
测试隔离                  每个测试类应独立运行，不相互依赖
BaseTest                  统一管理初始化和公共方法
Allure 附件               attachText / attachJson 添加到报告
冒烟测试                  只测试核心功能，快速验证系统可用性


========================================
七、7 天学习成果总览
========================================

天数    内容                        用例数
─────────────────────────────────────────────
Day 1   Web UI 自动化（Selenium）    3
Day 2   接口自动化入门（RestAssured） 3
Day 3   接口关联 + 数据驱动          6
Day 4   框架封装（ApiClient）        3
Day 5   框架深度封装 + POJO          4
Day 6   数据驱动 + Allure 报告       8
Day 7   框架整合 + testng.xml 优化   11
─────────────────────────────────────────────
合计                                38 个用例 ✅


========================================
八、项目最终结构
========================================

auto-test-day07/
├── pom.xml
├── testng.xml
├── README.md
└── src/
    └── test/
        ├── java/
        │   └── com/
        │       └── yejunlong/
        │           ├── base/
        │           │   └── BaseTest.java
        │           ├── config/
        │           │   └── ApiConfig.java
        │           ├── client/
        │           │   └── ApiClient.java
        │           ├── utils/
        │           │   ├── AssertUtils.java
        │           │   ├── JsonUtils.java
        │           │   ├── DataLoader.java
        │           │   └── PropertiesUtils.java
        │           ├── model/
        │           │   ├── AuthRequest.java
        │           │   ├── AuthResponse.java
        │           │   ├── BookingRequest.java
        │           │   └── BookingResponse.java
        │           ├── api/
        │           │   ├── AuthApi.java
        │           │   └── BookingApi.java
        │           └── tests/
        │               ├── BookingSmokeTest.java
        │               └── BookingDataDrivenTest.java
        └── resources/
            ├── application.properties
            ├── logback.xml
            ├── testdata.json
            └── testdata.yaml


========================================
总结
========================================

7 天完成了从零到企业级接口自动化测试框架的搭建：

  ✅ 从 Web UI 到接口自动化
  ✅ 从硬编码到数据驱动
  ✅ 从控制台输出到 Allure 报告
  ✅ 从零散脚本到分层框架
  ✅ 38 个测试用例全部通过

你已经具备了初级自动化测试工程师的核心能力！
