# Day 02 - 接口自动化测试入门

**学习日期：** 2026年7月16日

---

## 📌 今日学习目标

- [x] 理解接口测试的价值和适用场景
- [x] 掌握 RestAssured 框架的基本用法
- [x] 学会发送 GET 请求并验证响应
- [x] 掌握常用的断言方式（状态码、响应体字段）
- [x] 完成 3 个真实的 API 测试用例

---

## 🛠️ 环境配置

| 工具 | 版本 | 说明 |
|------|------|------|
| RestAssured | 5.3.2 | Java 接口测试框架 |
| TestNG | 7.8.0 | 测试用例管理 |
| Jackson | 2.15.2 | JSON 解析 |
| 测试 API | GitHub API | 真实的公开 API |

---

## 🎯 为什么今天学接口自动化？

| 对比项 | Web UI 自动化 | 接口自动化 |
|--------|---------------|------------|
| 执行速度 | 慢（需要打开浏览器） | 快（毫秒级） |
| 稳定性 | 受页面变化影响 | 稳定（接口变化频率低） |
| 维护成本 | 高（元素定位经常变） | 低 |
| 企业需求 | 有 | **更大** |

> **你的优势**：你懂 Java + Spring Boot，接口测试对你来说就像"用代码调用自己写的 Controller"一样自然。

---

## 💻 RestAssured 核心语法

### 基本格式

given()           // 准备请求（参数、Header、Body）
    .参数配置
.when()            // 发送请求（GET/POST/PUT/DELETE）
    .请求方法("路径")
.then()            // 验证响应（状态码、响应体）
    .断言验证


示例代码

// 发送 GET 请求并验证
given()
    .pathParam("username", "octocat")     // 路径参数
.when()
    .get("/users/{username}")              // 发送请求
.then()
    .statusCode(200)                       // 验证状态码
    .body("login", equalTo("octocat"));   // 验证响应体
📝 完成的测试用例
测试用例	验证内容	状态
testGetUser	获取用户 octocat 的信息	✅ 通过
testSearchRepositories	搜索 selenium 仓库	✅ 通过
testUserNotFound	不存在的用户返回 404	✅ 通过
运行结果
text
✅ 测试通过：成功获取用户 octocat 的信息
✅ 测试通过：搜索 selenium 仓库成功
✅ 测试通过：不存在的用户返回 404

===============================================
Default Suite
Total tests run: 3, Passes: 3, Failures: 0, Skips: 0
===============================================
🧠 今日踩坑记录
问题：断言 items.size() == 5 失败
错误信息：

text
JSON path items doesn't match.
Expected: <5>
Actual: <[{id=7613257, ...}]>
原因分析：

我期望返回 5 条结果，但 GitHub API 实际返回的数量可能不等于 5

接口返回的结果数量受多种因素影响（排序、评分、数据变化）

解决方案：

java
// ❌ 错误写法：断言精确值
.body("items.size()", equalTo(5))

// ✅ 正确写法：断言大于0
.body("items.size()", greaterThan(0))
核心教训：
接口测试中，对于搜索结果、列表数量等动态数据，不要断言精确值，应该断言范围（如 > 0）或字段存在（如 notNullValue()）。

📊 RestAssured 常用断言速查表
断言方式	代码示例	说明
状态码	.statusCode(200)	验证 HTTP 状态码
精确匹配	.body("name", equalTo("octocat"))	验证字段等于预期值
大于	.body("id", greaterThan(0))	验证字段大于 0
包含字符串	.body("message", containsString("Not Found"))	验证字段包含指定文字
不为空	.body("items", not(emptyArray()))	验证数组不为空
不为 null	.body("name", notNullValue())	验证字段不为 null
🔗 RestAssured 对比 Spring Boot
RestAssured 写法	对应 Spring Boot 概念
baseURI = "https://api.github.com"	@RequestMapping("/api")
pathParam("username", "octocat")	@PathVariable("username")
queryParam("q", "selenium")	@RequestParam("q")
get("/users/{username}")	@GetMapping
statusCode(200)	ResponseEntity.ok()
body("login", equalTo("octocat"))	Assert.assertEquals()
📁 项目结构
text
auto-test-learning/
├── Day02-接口自动化测试/
│   ├── README.md                    # 学习总结（本文件）
│   └── FirstApiTest.java            # 接口测试代码
├── drivers/
│   └── msedgedriver.exe             # Edge 驱动
└── pom.xml                          # Maven 配置（含 RestAssured）
📚 参考资料
RestAssured 官方文档

GitHub API 文档

TestNG 官方文档

JSONPath 语法参考


