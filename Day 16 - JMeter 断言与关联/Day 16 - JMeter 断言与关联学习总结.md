# Day 16 - JMeter 断言与关联

**学习日期：** 2026年8月3日


## 📌 今日目标

掌握 JMeter 的 JSON 断言、响应断言和 JSON 提取器，实现接口间的数据关联。


## ✅ 今日成果

| 任务 | 状态 |
|------|------|
| 添加 JSON 断言，验证 `status` 字段 | ✅ |
| 添加响应断言，验证响应包含 `"OK"` | ✅ |
| 添加 JSON 提取器，提取 `weather` 字段 | ✅ |
| 添加 JSON 提取器，提取 `temperature` 字段 | ✅ |
| 添加 JSON 提取器，提取 `humidity` 字段 | ✅ |
| Debug Sampler 验证变量提取成功 | ✅ |


## 📊 验证结果

Debug Sampler 输出：
weather=中雨
temperature=31
humidity=75
weather_matchNr=1
temperature_matchNr=1
humidity_matchNr=1

text


## 📝 今日核心配置

### 1. JSON 断言配置

| 配置项 | 值 |
|--------|-----|
| **JSON Path** | `$.status` |
| **Expected Value** | `"1"` |
| **Match as regular expression** | 取消勾选 |

### 2. JSON 提取器配置（提取 weather）

| 配置项 | 值 |
|--------|-----|
| **Variable names** | `weather` |
| **JSON Path expressions** | `$.lives[0].weather` |
| **Match No.** | `1` |
| **Default Values** | `未知` |

### 3. Debug Sampler 配置

| 配置项 | 值 |
|--------|-----|
| **JMeter properties** | `False` |
| **JMeter variables** | `True` |
| **System properties** | `False` |


## 🐛 今日踩坑记录

### 问题1：一个 JSON Extractor 提取多个字段失败

**现象：** `Variable names: weather,temperature,humidity` 配置后提取失败

**原因：** JMeter 对多字段提取的 `Match No.` 解析不稳定

**解决：** 使用三个独立的 JSON Extractor，每个提取一个字段

### 问题2：Debug Sampler 不显示变量

**原因：** JSON Extractor 必须放在 HTTP Request 的**子级**位置

**解决：** 右键点击 HTTP Request → Add → Post Processors → JSON Extractor


## 💡 核心知识点总结

| 知识点 | 说明 |
|--------|------|
| JSON 断言 | 精确校验 JSON 响应中的字段 |
| 响应断言 | 校验响应文本是否包含指定内容 |
| JSON 提取器 | 从 JSON 响应中提取数据 |
| 接口关联 | 将提取的数据传递给后续请求 |
| Debug Sampler | 调试用，查看所有变量值 |
| `_matchNr` | 匹配数量，>0 表示成功 |


## 📁 今日文件
D:\JMeterTestPlans
└── 高德天气API_断言与关联.jmx

text


## 📌 今日总结

今天完成了 Day 16 的任务：JMeter 断言与关联。

关键收获：
- 掌握了 JSON 断言和响应断言的配置方法
- 学会了用 JSON 提取器从响应中提取数据
- 理解了 Debug Sampler 的调试用法
- 成功提取了天气数据（中雨、31°C、75%）
