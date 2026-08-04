# Day 17 - JMeter 分布式压测

**学习日期：** 2026年8月4日


## 📌 今日目标

了解 JMeter 分布式压测架构，配置 Master-Slave 模式，实现多台机器联合压测。


## ✅ 今日成果

| 任务 | 状态 |
|------|------|
| 理解分布式压测概念 | ✅ |
| 查看本机 IP 地址 | ✅ |
| 配置 user.properties | ✅ |
| 启动 Slave 服务 | ✅ |
| 固定 RMI 端口 | ✅ |
| 执行分布式测试 | ✅ |


## 📊 执行结果
Starting the test on host 192.168.10.13:1099 @ 2026 Aug 4 17:26:45
Finished the test on host 192.168.10.13:1099 @ 2026 Aug 4 17:27:12

text

| 指标 | 数据 |
|------|------|
| 测试开始时间 | 17:26:45 |
| 测试结束时间 | 17:27:12 |
| 总耗时 | 约 27 秒 |
| Slave 状态 | ✅ 成功执行 |


## 🧱 分布式压测架构
┌─────────────┐
│ Master │ ← 控制机（调度测试）
│ (Controller)│
└──────┬──────┘
│
┌───────────────┼───────────────┐
│ │ │
▼ ▼ ▼
┌───────────┐ ┌───────────┐ ┌───────────┐
│ Slave 1 │ │ Slave 2 │ │ Slave 3 │ ← 执行机（发送请求）
│ (执行机) │ │ (执行机) │ │ (执行机) │
└───────────┘ └───────────┘ └───────────┘

text

**Master：** 控制机，负责调度测试、收集结果  
**Slave：** 执行机，负责发送请求、执行测试


## 📝 今日核心配置

### 1. 查看本机 IP

```bash
ipconfig
本机 IP： 192.168.10.13

2. user.properties 配置
在 JMETER_HOME/bin/user.properties 中添加：

properties
# ========== 分布式测试配置 ==========
server.rmi.ssl.disable=true
server_port=1099
java.rmi.server.hostname=192.168.10.13
remote_hosts=192.168.10.13:1099
3. 启动 Slave
bash
cd /d D:\Program Files\VMeter\apache-jmeter-5.6.3\bin
jmeter-server.bat
成功标志：

text
Created remote object: UnicastServerRef2 [liveRef: [endpoint:[192.168.10.13:1099](local),...]]
4. 执行分布式测试
在 JMeter GUI 中：

Run → Remote Start → 选择 192.168.10.13:1099

或使用命令行：

bash
jmeter -n -t test.jmx -R 192.168.10.13:1099 -l result.jtl
🐛 今日踩坑记录
问题1：权限不足，无法修改 user.properties
现象： 记事本提示"你没有权限打开该文件"

原因： JMeter 安装在 Program Files 目录，普通用户没有写权限

解决： 以管理员身份运行记事本，或把 JMeter 移到非系统目录

问题2：配置被注释，未生效
现象： 配置了 server.rmi.ssl.disable=true 但仍然报 SSL 错误

原因： 配置行前面有 #，被 JMeter 当成了注释

解决： 删除配置前的 #

问题3：端口不一致，找不到 Slave
现象： no such object in table

原因： Slave 实际使用的端口和 Master 配置的端口不一致

解决： 固定 server_port=1099，让两端端口保持一致

📊 配置参数说明
参数	说明	示例
server.rmi.ssl.disable	禁用 RMI SSL（分布式必需）	true
server_port	Slave RMI 服务端口	1099
java.rmi.server.hostname	Slave 机器的 IP	192.168.10.13
remote_hosts	Master 连接的 Slave 列表	192.168.10.13:1099
client.rmi.localport	Master RMI 本地端口（可选）	1101
💡 核心知识点总结
知识点	说明
分布式压测	多台机器联合压测，突破单机瓶颈
Master	控制机，调度测试，收集结果
Slave	执行机，发送请求
RMI	Java 远程方法调用，用于 Master-Slave 通信
server_port	Slave 的 RMI 服务端口（默认 1099）
remote_hosts	Master 配置 Slave 列表
user.properties	JMeter 用户配置文件，覆盖默认配置
📁 今日配置位置
text
D:\Program Files\VMeter\apache-jmeter-5.6.3\bin\
├── jmeter.bat          ← JMeter 启动脚本
├── jmeter-server.bat   ← Slave 启动脚本
├── jmeter.properties   ← 主配置文件（不修改）
└── user.properties     ← 用户配置文件（✅ 修改此文件）
📌 今日总结
今天完成了 Day 17 的任务：JMeter 分布式压测。

关键收获：

理解了分布式压测的 Master-Slave 架构

学会了配置 Slave 服务

掌握了固定 RMI 端口的方法

成功执行了分布式测试

学会了排查分布式常见问题（端口不一致、SSL 错误等）
