# Day 11 - Jenkins 持续集成入门 + Git 仓库整理

**学习日期：** 2026年7月28日


## 📌 今日目标

1. 安装并配置 Jenkins，建立持续集成流水线
2. 将自动化测试项目接入 Jenkins，实现自动构建和测试
3. 整理 Git 仓库结构，将第 11 天代码归档到独立文件夹


## ✅ 今日成果

| 任务 | 状态 |
|------|------|
| Jenkins 安装与配置 | ✅ |
| Jenkins 任务创建与构建 | ✅ |
| Git 仓库整理 | ✅ |
| Day11 代码归档到独立文件夹 | ✅ |


## 🧱 今日核心内容

### 1. Jenkins 安装与启动

**下载方式：** 使用 `.war` 文件启动

```bash
java -jar jenkins.war
访问地址： http://localhost:8080

初始密码位置： C:\Users\用户名\.jenkins\secrets\initialAdminPassword

2. Jenkins 任务配置
任务类型： Freestyle project

源码管理：

Repository URL：https://github.com/CodeRiley-new/auto-test-learning.git

Branches to build：*/master

构建步骤：

Invoke top-level Maven targets → Goals: clean test

测试报告配置：

Publish JUnit test result report → target/surefire-reports/*.xml

3. Git 仓库整理
整理前： 第 11 天代码散落在仓库根目录（.gitignore、pom.xml、src/、logs/）

整理后： 所有文件移至 Day11-Jenkins持续集成入门/ 文件夹

操作命令：

bash
git add .
git commit -m "chore: 整理第11天代码到 Day11-Jenkins持续集成入门 文件夹"
git pull origin master --allow-unrelated-histories
git push origin master
📁 最终项目结构
text
auto-test-learning/
├── Day01-SauceDemo/
├── Day02-接口自动化测试/
├── auto-test-day03/
├── day04-接口自动化测试封装/
├── Day05-接口测试框架深度封装+数据分离/
├── Day06-数据驱动+Allure报告+持续集成入门/
├── Day07-测试框架整合与优化/
├── Day08-框架独立搭建实战/
├── day09-JDBC+数据库断言/
├── Day10-日志与异常处理/
└── Day11-Jenkins持续集成入门/    ← 新建
    ├── .gitignore
    ├── pom.xml
    ├── logs/
    └── src/
🐛 今日踩坑记录
问题1：Git 命令找不到
错误： 'git' 不是内部或外部命令

原因： Git 未安装或未添加到系统环境变量

解决： 安装 Git 后，将 C:\Program Files\Git\bin 和 C:\Program Files\Git\cmd 添加到系统 PATH

问题2：Jenkins 找不到测试报告
错误： target/surefire-reports/*.xml doesn't match anything

原因： 构建步骤中没有执行 mvn test，或构建失败没有生成报告

解决： 在 Jenkins 构建步骤中添加 clean test，并先确认本地 mvn clean test 能成功执行

问题3：Git 合并时进入 Vim 编辑器
现象： 合并时弹出 Vim 编辑界面，无法退出

解决： 按 Esc 退出编辑模式，输入 :wq 保存并退出，或使用 git commit --no-edit 跳过编辑信息

问题4：推送被拒
错误： 推送被拒，需要合并远程更改

解决： 执行 git pull origin master --allow-unrelated-histories，解决冲突后再次推送

问题5：Git 连接被重置
错误： Recv failure: Connection was reset

原因： 代理未配置或代理工具未运行

解决： 配置 Git 代理

bash
git config --global http.proxy http://127.0.0.1:7897
git config --global https.proxy http://127.0.0.1:7897
💡 核心知识点总结
知识点	说明
持续集成（CI）	代码提交后自动构建、测试，快速反馈问题
Jenkins	最流行的 CI 工具，支持插件扩展
Freestyle 项目	Jenkins 中最基础的任务类型
Jenkins 工作空间	Jenkins 从 GitHub 拉取代码的目录
Git 分支管理	统一使用 master 分支作为主分支
Git 代理配置	解决国内访问 GitHub 的网络问题
📌 今日总结
今天完成了 Day 11 的任务：Jenkins 持续集成入门 + Git 仓库整理。

关键收获：

成功安装并配置 Jenkins，实现自动构建和测试

掌握了 Git 分支合并和仓库整理的操作

解决了代理配置、Vim 编辑器、推送冲突等实际问题

将第 11 天代码归档到独立文件夹，保持项目结构整洁
