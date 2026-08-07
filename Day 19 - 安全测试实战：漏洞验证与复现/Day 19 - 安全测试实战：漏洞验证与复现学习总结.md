**学习日期：** 2026年8月7日


## 📌 今日目标

手动验证 ZAP 扫描发现的漏洞，学习漏洞复现技巧，加深对 Web 安全问题的理解。


## ✅ 今日成果

| 任务 | 状态 |
|------|------|
| 理解漏洞验证方法 | ✅ |
| 使用开发者工具验证 CSP 缺失 | ✅ |
| 学习 XSS 攻击原理 | ✅ |
| 验证点击劫持漏洞 | ✅ |
| 编写漏洞复现报告 | ✅ |


## 📊 验证的漏洞

| 序号 | 漏洞名称 | 验证方法 | 结果 |
|------|----------|----------|------|
| 1 | CSP 响应头缺失 | 浏览器开发者工具 | ✅ 确认存在 |
| 2 | 缺少 X-Frame-Options | 创建 HTML iframe 测试 | ✅ 确认存在 |
| 3 | XSS 攻击演示 | 本地 DVWA 环境 | ✅ 学习原理 |


## 🧪 漏洞验证方法

### 1. CSP 响应头缺失验证

**验证步骤：**

1. 打开 Chrome/Edge 浏览器，访问 `https://restful-booker.herokuapp.com`
2. 按 `F12` 打开开发者工具
3. 点击 **Network** 标签
4. 刷新页面（`F5`）
5. 点击第一个请求（根路径）
6. 查看 **Response Headers** 中的 `Content-Security-Policy`

**验证结果：**
❌ Content-Security-Policy: (不存在)

text

**结论：** 漏洞真实存在，ZAP 扫描结果准确。

### 2. 点击劫持漏洞验证

**验证步骤：**

创建 `clickjacking-test.html` 文件：

```html
<!DOCTYPE html>
<html>
<head>
    <title>点击劫持测试</title>
</head>
<body>
    <h1>点击劫持漏洞测试</h1>
    <p>如果下方能显示内容，说明存在点击劫持风险。</p>
    <iframe src="https://restful-booker.herokuapp.com" width="800" height="500"></iframe>
</body>
</html>
验证结果：

页面内容成功嵌入 iframe ✅

缺少 X-Frame-Options 或 frame-ancestors 保护 ❌

结论： 网站存在点击劫持风险。

3. XSS 原理学习
在本地 DVWA 中测试：

在 XSS (Reflected) 模块中输入：

html
<script>alert('XSS 漏洞演示')</script>
原理： 服务器未对用户输入进行过滤，直接将恶意脚本返回给浏览器执行。

🛠️ 使用的工具
工具	用途
浏览器开发者工具	查看响应头，验证 CSP 缺失
HTML 测试页面	验证点击劫持漏洞
DVWA	学习和验证 XSS 攻击原理
Burp Suite Community	拦截请求，查看完整响应头
📝 漏洞复现报告模板
markdown
# 漏洞复现报告

## 漏洞1：Content-Security-Policy 响应头缺失

### 基本信息
- **漏洞名称：** Content-Security-Policy Header Not Set
- **风险等级：** 中危
- **测试日期：** 2026-08-07
- **测试地址：** https://restful-booker.herokuapp.com

### 复现步骤
1. 打开浏览器开发者工具（F12）
2. 访问 https://restful-booker.herokuapp.com
3. 在 Network 标签中查看 Response Headers
4. 确认 Content-Security-Policy 头不存在

### 影响范围
- 整个网站所有页面

### 潜在危害
- 增加 XSS 攻击风险
- 缺乏对资源加载的控制
- 无法防御数据注入攻击

### 修复建议
在服务器配置中添加 CSP 响应头：
```http
Content-Security-Policy: default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self';
参考链接
https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Guides/CSP

https://cheatsheetseries.owasp.org/cheatsheets/Content_Security_Policy_Cheat_Sheet.html


text


## 📌 今日总结

今天完成了 Day 19 的任务：安全测试实战 - 漏洞验证与复现。

关键收获：
- 学会了手动验证安全漏洞的方法
- 理解了 CSP 缺失和点击劫持漏洞的实际影响
- 学会了编写专业的漏洞复现报告
- 掌握了 XSS 攻击的基本原理
- 能够向开发团队清晰地描述漏洞和修复方案
