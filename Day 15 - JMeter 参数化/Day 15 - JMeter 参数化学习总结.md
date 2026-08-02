# Day 15 - JMeter 参数化

**学习日期：** 2026年8月2日


## 📌 今日目标

掌握 JMeter 的 CSV 参数化功能，用多组数据驱动测试，实现数据与脚本分离。


## ✅ 今日成果

| 任务 | 状态 |
|------|------|
| 创建 CSV 数据文件 | ✅ |
| 配置 CSV Data Set Config | ✅ |
| 参数化 HTTP Request | ✅ |
| 多城市数据驱动测试 | ✅ |
| 错误率降为 0% | ✅ |


## 📊 测试结果

| 指标 | 数值 |
|------|------|
| 测试数据行数 | 10 个城市 |
| 并发用户数 | 10 |
| 循环次数 | 2 |
| 总请求数 | 20 |
| 错误率 | 0% ✅ |
| 响应状态码 | 200 |


## 📝 今日核心配置

### 1. CSV 数据文件（cities.csv）

```csv
cityCode,cityName,adcode
101010100,北京,110000
101020100,上海,310000
101280101,广州,440100
101280601,深圳,440300
101040100,重庆,500000
101010200,天津,120000
101110101,西安,610100
101200101,武汉,420100
101070101,成都,510100
101050101,哈尔滨,230100
2. CSV Data Set Config 配置
配置项	值
Filename	D:/JMeterTestPlans/cities.csv
File encoding	UTF-8
Variable Names	cityCode,cityName,adcode
Delimiter	,
Recycle on EOF	True
Stop thread on EOF	False
Sharing mode	All threads
3. HTTP Request 参数
参数名	值
city	${adcode}
key	你的高德Web服务API Key
extensions	base
output	JSON
4. 线程组配置
参数	值
Number of Threads	10
Ramp-up period	30 秒
Loop Count	2
🐛 今日踩坑记录
问题1：QPS 限流导致部分请求失败
现象： 部分请求返回 {"status":"0","info":"CUQPS_HAS_EXCEEDED_THE_LIMIT","infocode":"10021"}

原因： 高德 API 有 QPS 限制，瞬时并发过高被限流

解决： 增加 Ramp-up 时间至 30 秒，让用户缓慢启动

问题2：JMeter 错误率不准确
现象： 修改配置后重新运行，错误率仍显示 15%

原因： 没有清除旧数据，新旧数据混合在一起

解决： 每次运行前点击 清除按钮（🧹） 或按 Ctrl + E

问题3：CSV 变量未正确引用
现象： 请求 URL 中显示 city=101010100,北京,110000

原因： 错误地引用了整行数据而非单列

解决： 在 HTTP Request 中只引用 ${adcode}，而非整行

问题4：CSV 分隔符不匹配
现象： JMeter 无法正确读取 CSV 数据

原因： Windows Excel 导出的 CSV 可能使用 ; 而非 ,

解决： 检查 CSV 文件实际分隔符，在 CSV Data Set Config 中配置正确的 Delimiter

📊 参数化流程
text
CSV 文件
    ↓
CSV Data Set Config（读取数据）
    ↓
JMeter 变量（${cityCode}、${cityName}、${adcode}）
    ↓
HTTP Request（引用 ${adcode}）
    ↓
发送请求到高德 API
    ↓
返回对应城市的天气数据
数据流转示例
第几次循环	使用的数据	请求的 city 参数
第 1 次	北京 (110000)	city=110000
第 2 次	上海 (310000)	city=310000
第 3 次	广州 (440100)	city=440100
...	...	...
💡 核心知识点总结
知识点	说明
参数化	用外部数据替换硬编码值，实现数据驱动测试
CSV Data Set Config	JMeter 读取 CSV 文件的配置元件
Variable Names	定义 CSV 各列对应的变量名
${变量名}	JMeter 中引用变量的语法
数据驱动测试	用多组数据执行同一测试
QPS 限流	API 对每秒请求数的限制，需合理控制并发
清除机制	JMeter 的旧数据会累积，每次运行前需清除
📁 今日文件
text
D:\JMeterTestPlans\
├── 高德天气API压测.jmx    ← 主测试计划
└── cities.csv              ← CSV 数据文件
📌 今日总结
今天完成了 Day 15 的任务：JMeter 参数化。

关键收获：

掌握了 CSV 参数化的完整流程

学会了处理 QPS 限流问题

理解了 JMeter 清除机制的重要性

实现了多城市数据驱动测试

错误率成功降为 0%
