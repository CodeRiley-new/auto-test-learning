# Day 18 - 安全测试入门

**学习日期：** 2026年8月6日


## 📌 今日目标

了解 Web 安全测试的基本概念，使用 OWASP ZAP 对真实网站进行安全扫描，分析并理解常见安全漏洞。


## ✅ 今日成果

| 任务 | 状态 |
|------|------|
| 配置 ZAP 代理连接目标网站 | ✅ |
| 对 restful-booker 进行安全扫描 | ✅ |
| 发现并分析 CSP 相关漏洞 | ✅ |
| 学习 CSP 安全策略原理 | ✅ |
| 记录漏洞分析和修复建议 | ✅ |


## 📊 发现的漏洞汇总

| 序号 | 漏洞名称 | 风险等级 | 位置 |
|------|----------|----------|------|
| 1 | CSP: Failure to Define Directive with No Fallback | Medium | `/apidoc/vendor` |
| 2 | Content Security Policy (CSP) Header Not Set | Medium | 根路径 `/` |


## 🐛 漏洞详解

### 漏洞1：CSP 指令缺失（中危）

**漏洞名称：** CSP: Failure to Define Directive with No Fallback

**位置：** `https://restful-booker.herokuapp.com/apidoc/vendor`

**问题描述：**
网站设置了 `default-src 'none'`（默认禁止所有外部资源），但漏掉了两个重要指令：

| 缺失指令 | 作用 | 风险 |
|----------|------|------|
| `frame-ancestors` | 防止网站被嵌入 iframe | 可能被点击劫持攻击 |
| `form-action` | 限制表单提交目标 | 可能被钓鱼攻击利用 |

**修复方案：**
```http
Content-Security-Policy: default-src 'none'; frame-ancestors 'self'; form-action 'self';
漏洞2：CSP 响应头未设置（中危）
漏洞名称： Content Security Policy (CSP) Header Not Set

位置： https://restful-booker.herokuapp.com/

问题描述：
根路径完全没有配置 CSP 响应头，网站缺乏对 XSS 和数据注入攻击的基础防护。

修复方案：
在 Nginx 或应用层配置 CSP 响应头：

http
Content-Security-Policy: default-src 'self'; script-src 'self'; style-src 'self';
🧠 核心知识点
什么是 CSP（内容安全策略）？
CSP 是一种浏览器安全机制，通过 HTTP 响应头告诉浏览器哪些资源是安全的，可以加载。

CSP 能防御的攻击类型：

✅ XSS（跨站脚本攻击）

✅ 数据注入攻击

✅ 点击劫持

✅ 数据泄露

CSP 常见指令
指令	作用
default-src	默认资源加载策略
script-src	限制 JavaScript 来源
style-src	限制 CSS 来源
img-src	限制图片来源
frame-ancestors	限制 iframe 嵌入
form-action	限制表单提交目标
OWASP ZAP 工具
功能	说明
主动扫描	主动发送攻击请求检测漏洞
被动扫描	分析正常请求响应发现漏洞
Spider	爬取网站所有页面
Alerts	显示发现的所有漏洞
Report	导出安全测试报告
💡 面试准备
面试官可能会问：
Q: 什么是 CSP？它的作用是什么？

A: CSP（内容安全策略）是一种浏览器安全机制，通过 HTTP 响应头告诉浏览器哪些资源可以加载。它可以有效防御 XSS、数据注入和点击劫持等攻击。

Q: 你在安全测试中发现过什么漏洞？

A: 在实习练习中，我使用 OWASP ZAP 对测试网站进行扫描，发现目标网站存在 CSP 配置缺失和指令不完整的问题，属于中危漏洞。我建议补充完整的 CSP 响应头，并完善 frame-ancestors 和 form-action 等关键指令的配置。

Q: 你认为安全测试在自动化测试工程师的职责中占什么位置？

A: 安全测试通常是专门的团队负责。作为自动化测试工程师，我们主要关注接口功能测试和性能测试，但了解常见的安全漏洞类型有助于我们在开发测试代码时，能够识别并排查一些潜在的安全问题，比如在接口测试中注意敏感信息是否在响应中返回。

📁 今日文件
text
安全测试报告/
└── ZAP_Scan_Report_20260806.html   (从 ZAP 导出)
📌 今日总结
今天完成了 Day 18 的任务：安全测试入门。

关键收获：

了解 CSP（内容安全策略）的作用和配置方式

使用 OWASP ZAP 成功发现中危漏洞

学会阅读和分析安全扫描报告

能够描述漏洞并提出修复方案
