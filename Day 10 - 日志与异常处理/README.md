# Day 10 - 日志与异常处理

**学习日期：** 2026年7月26日


## 📌 今日目标

在测试框架中集成 Logback 日志框架，用专业日志替代 System.out.println()，并学会查看和管理日志文件。


## ✅ 今日成果

| 测试用例 | 状态 |
|----------|------|
| testLogin | ✅ 通过 |
| testCreateBooking | ✅ 通过 |
| testGetBooking | ✅ 通过 |
| **合计** | **3/3 全部通过** |


## 🧱 今日新增内容

| 文件 | 作用 |
|------|------|
| logback.xml | 日志配置文件（控制台输出 + 文件输出） |
| BookingLogTest.java | 使用日志的测试类 |
| logs/test.log | 日志文件（运行后自动生成） |


## 📦 第一步：添加日志依赖

在 `pom.xml` 中添加：

```xml
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.4.14</version>
</dependency>
⚙️ 第二步：logback.xml 配置
在 src/test/resources/logback.xml：

xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <!-- 控制台输出 -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%-5level] %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- 文件输出 -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/test.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/test.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
    </root>

</configuration>
💻 第三步：在测试中使用日志
java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookingLogTest {

    private static final Logger log = LoggerFactory.getLogger(BookingLogTest.class);

    @Test
    public void testLogin() {
        log.info("========== 开始测试：登录 ==========");
        // ... 测试代码 ...
        log.info("✅ 登录成功，Token：{}", authToken);
        log.info("========== 登录测试完成 ==========");
    }
}
📊 日志级别说明
级别	颜色	用途
ERROR	红色	错误信息，测试失败时
WARN	黄色	警告信息，配置问题时
INFO	绿色	关键流程信息
DEBUG	蓝色	调试信息，变量值
TRACE	灰色	最详细的信息
📂 如何查看日志文件
方式一：IDEA 中直接查看
在 IDEA 左侧 Project 面板中

展开项目根目录

找到 logs 文件夹

双击 test.log 文件

方式二：文件管理器查看
项目根目录下的 logs/test.log

方式三：命令行查看
bash
# Linux/Mac
tail -20 logs/test.log

# Windows PowerShell
Get-Content logs\test.log -Tail 20
🐛 今日踩坑记录
问题：找不到 logs 文件夹

原因：还没运行过测试，Logback 没有创建日志文件。

解决：先运行一次测试，logs 文件夹和 test.log 文件会自动生成。

💡 System.out.println() vs 日志
对比项	System.out.println()	Logback 日志
时间戳	❌ 无	✅ 有
日志级别	❌ 无	✅ INFO/WARN/ERROR
灵活开关	❌ 不能	✅ 可配置
持久化	❌ 控制台消失就没了	✅ 自动写入文件
格式统一	❌ 每个项目不同	✅ 统一配置
异常堆栈	❌ 不完整	✅ 完整打印
📁 今日项目结构
text
auto-test-day10/
├── pom.xml
├── logs/
│   └── test.log                    ← 自动生成
└── src/
    └── test/
        ├── java/
        │   └── com/
        │       └── yejunlong/
        │           ├── config/
        │           │   └── ApiConfig.java
        │           ├── client/
        │           │   └── ApiClient.java
        │           ├── utils/
        │           │   ├── AssertUtils.java
        │           │   ├── JsonUtils.java
        │           │   └── DbUtils.java
        │           ├── model/
        │           │   ├── AuthRequest.java
        │           │   ├── AuthResponse.java
        │           │   ├── BookingRequest.java
        │           │   └── BookingResponse.java
        │           ├── api/
        │           │   ├── AuthApi.java
        │           │   └── BookingApi.java
        │           └── tests/
        │               └── BookingLogTest.java    ← 新增
        └── resources/
            └── logback.xml                       ← 新增
📌 今日总结
今天完成了 Day 10 的任务：在测试框架中集成 Logback 日志。

关键收获：

掌握了 Logback 的配置和使用

理解了日志级别的作用

学会了查看日志文件

用专业日志替代了 System.out.println()
