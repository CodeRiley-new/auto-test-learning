# Day 14 - JMeter 入门与接口压测

**学习日期：** 2026年7月31日


## 📌 今日目标

掌握 JMeter 的基本使用，对真实 API 进行压测，理解性能测试的核心指标。


## ✅ 今日成果

| 任务 | 状态 |
|------|------|
| 安装并启动 JMeter | ✅ |
| 创建测试计划 | ✅ |
| 配置线程组（10 用户，2 次循环） | ✅ |
| 配置 HTTP 请求（高德天气 API） | ✅ |
| 添加响应断言 | ✅ |
| 运行测试并分析结果 | ✅ |


## 📊 测试结果

| 指标 | 数值 |
|------|------|
| 总请求数 | 20（10 用户 × 2 次循环） |
| 响应状态码 | 200 ✅ |
| 错误率 | 0%（排除业务错误） |
| 平均响应时间 | 200-500ms |
| 业务错误码 | 10021（单日调用量超限） |


## 🧱 今日核心配置

### 1. 测试计划结构
Test Plan: 高德天气API压测
└── Thread Group (10 users, 5s ramp-up, 2 loops)
└── HTTP Request
├── Protocol: https
├── Server: restapi.amap.com
├── Path: /v3/weather/weatherInfo
└── Parameters:
├── city: 110101
├── extensions: base
├── output: JSON
└── key: 你的API Key
└── HTTP Header Manager
└── Content-Type: application/json
└── Response Assertion
└── Contains: "status":"1"
└── Listeners:
├── View Results Tree
├── Aggregate Report
└── Graph Results

text

### 2. JMeter 核心组件说明

| 组件 | 作用 |
|------|------|
| **Thread Group** | 模拟并发用户，配置用户数、启动时间、循环次数 |
| **HTTP Request** | 发送 HTTP 请求，配置协议、服务器、路径、参数 |
| **HTTP Header Manager** | 设置请求头（如 Content-Type） |
| **Response Assertion** | 验证响应内容是否符合预期 |
| **View Results Tree** | 查看每个请求的详细结果 |
| **Aggregate Report** | 查看聚合统计数据 |

### 3. 线程组参数

| 参数 | 值 | 说明 |
|------|-----|------|
| Number of Threads | 10 | 并发用户数 |
| Ramp-up period | 5s | 5 秒内启动全部用户 |
| Loop Count | 2 | 每个用户执行 2 次 |


## 🐛 今日踩坑记录

### 问题1：高德 API Key 类型错误

**错误：** `{"status":"0","info":"INVALID_USER_KEY","infocode":"10001"}`

**原因：** 使用了 JS API Key，而非 Web 服务 API Key

**解决：** 在高德控制台重新创建 Key，**服务平台选择 "Web 服务 API"**

### 问题2：天气 API 服务未开通

**错误：** `{"status":"0","info":"RESOURCE_UNAVAILABLE","infocode":"10017"}`

**原因：** API Key 未开通天气 API 服务

**解决：** 在高德控制台中，为应用开通"天气查询 API"服务

### 问题3：单日调用量超限

**错误：** `{"status":"0","info":"CUQPS_HAS_EXCEEDED_THE_LIMIT","infocode":"10021"}`

**原因：** 免费版 API 每日调用次数限制（1000 次/天）

**解决：** 次日重置，或申请提高免费限额

### 问题4：JSON 断言配置错误

**错误：** 请求返回 200，但 JMeter 显示红色

**原因：** JSON 断言中期望值格式错误，`1` 应为 `"1"`（字符串）

**解决：** 改用 Response Assertion，使用 Contains 匹配 `"status":"1"`


## 📊 高德 API 错误码速查

| 错误码 | 含义 | 解决方案 |
|--------|------|----------|
| 10000 | 请求成功 | 正常 |
| 10001 | Key 无效或类型错误 | 检查 Key 类型是否为 Web 服务 API |
| 10017 | 服务未开通 | 在控制台开通对应 API 服务 |
| 10021 | 单日调用量超限 | 次日重置，或申请提高限额 |


## 💡 核心知识点总结

| 知识点 | 说明 |
|--------|------|
| 性能测试 | 模拟多用户并发访问，检测系统性能 |
| 并发用户数 | 同时访问系统的用户数量 |
| 响应时间 | 从发送请求到收到响应的时间 |
| 吞吐量 | 单位时间内处理的请求数 |
| HTTP 200 vs 业务成功 | HTTP 200 只表示请求成功，业务可能失败 |
| 断言 | 验证响应内容是否符合预期 |


## 📁 今日文件
D:\JMeterTestPlans
└── 高德天气API压测.jmx

text


## 📌 今日总结

今天完成了 Day 14 的任务：JMeter 入门与接口压测。

关键收获：
- 掌握了 JMeter 的基本使用流程
- 学会配置线程组、HTTP 请求、断言和监听器
- 理解了 HTTP 状态码和业务错误码的区别
- 学会了查看和分析测试结果
- 遇到并解决了高德 API 的常见错误
