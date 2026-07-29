# Day 12 - Allure 报告与 Jenkins 深度集成

**学习日期：** 2026年7月29日


## 📌 今日目标

将 Allure 测试报告接入 Jenkins，让自动化测试结果更美观、更专业。


## ✅ 今日成果

| 任务 | 状态 |
|------|------|
| 在项目中使用 Allure 注解 | ✅ |
| 本地生成 Allure 报告 | ✅ |
| 在 Jenkins 中安装 Allure 插件 | ✅ |
| 配置 Jenkins 任务生成 Allure 报告 | ✅ |
| Allure 报告在 Jenkins 中成功展示 | ✅ |


## 📊 今日运行结果
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS

text

3 个测试用例全部通过，Allure 报告成功在 Jenkins 中展示。


## 🧱 今日核心配置

### 1. pom.xml 中的 Allure 配置

```xml
<properties>
    <allure.version>2.27.0</allure.version>
</properties>

<dependencies>
    <dependency>
        <groupId>io.qameta.allure</groupId>
        <artifactId>allure-testng</artifactId>
        <version>${allure.version}</version>
        <scope>test</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.0.0-M7</version>
            <configuration>
                <properties>
                    <property>
                        <name>listener</name>
                        <value>io.qameta.allure.testng.AllureTestNg</value>
                    </property>
                </properties>
                <systemPropertyVariables>
                    <allure.results.directory>
                        ${project.build.directory}/allure-results
                    </allure.results.directory>
                </systemPropertyVariables>
            </configuration>
        </plugin>
    </plugins>
</build>
2. 代码中的 Allure 注解
java
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;

@Epic("自动化测试框架")
@Feature("订单管理")
public class BookingAllureTest {

    @Test
    @Description("验证登录功能是否正常工作")
    @Story("用户登录")
    public void testLogin() {
        // 测试代码
    }

    @Test
    @Description("验证创建订单功能是否正常工作")
    @Story("创建订单")
    public void testCreateBooking() {
        // 测试代码
    }
}
3. Jenkins Allure 报告配置
构建后操作：

添加 Allure Report

Results 路径填写完整相对路径：

text
Day12-Allure 报告与 Jenkins 深度集成/target/allure-results
📋 Allure 常用注解
注解	作用
@Epic("项目名")	最顶层的大模块分类
@Feature("功能模块")	功能模块分类
@Story("具体功能")	具体功能点分类
@Description("描述")	为测试用例添加详细描述
@Severity(SeverityLevel.CRITICAL)	标记用例严重程度
@Step("步骤描述")	标记测试步骤
@Attachment	添加附件（日志、截图等）
🐛 今日踩坑记录
问题1：Allure 报告显示为空白
现象： Jenkins 中 Allure Report 页面打开后显示 "0 项"，没有任何测试数据。

原因： Allure 插件找不到 allure-results 目录，路径配置不正确。

解决： 在 Jenkins 的 Allure Report 构建后操作中，指定完整的相对路径：

text
Day12-Allure 报告与 Jenkins 深度集成/target/allure-results
问题2：Allure 命令行未配置
现象： Allure CLI not configured

解决： 在 Jenkins 的 Global Tool Configuration 中添加 Allure Commandline，勾选 Install automatically。

问题3：SSL/TLS 连接失败
现象： schannel: failed to receive handshake, SSL/TLS connection failed

解决： 配置 Git 代理或关闭 SSL 验证：

bash
git config --global http.proxy http://127.0.0.1:7897
git config --global https.proxy http://127.0.0.1:7897
git config --global http.sslVerify false
🔄 完整 CI 流水线
text
本地编写代码
    ↓ git push
GitHub 仓库
    ↓ Jenkins 拉取
Jenkins 构建（mvn clean test）
    ↓ 生成 Allure 数据
target/allure-results/*.json
    ↓ Allure 插件处理
Allure HTML 报告
    ↓ 展示
Jenkins 任务页面 Allure Report 图标
💡 核心知识点总结
知识点	说明
Allure	最流行的测试报告框架
allure-testng	Allure 与 TestNG 的适配器
@Feature/@Story	对测试进行分类
target/allure-results	Allure 原始数据目录
路径问题	Jenkins 中的路径是相对于工作空间的
📁 今日项目结构
text
auto-test-learning/
└── Day12-Allure 报告与 Jenkins 深度集成/
    ├── pom.xml
    ├── src/
    │   └── test/
    │       └── java/
    │           └── com/
    │               └── yejunlong/
    │                   └── tests/
    │                       └── BookingAllureTest.java
    └── logs/
📌 今日总结
今天完成了 Day 12 的任务：将 Allure 报告与 Jenkins 深度集成。

关键收获：

掌握了 Allure 在测试代码中的注解用法

学会了在 Jenkins 中配置 Allure 插件

解决了路径配置问题，让报告成功展示

打通了“测试执行 → 报告生成 → 可视化展示”的完整链路
