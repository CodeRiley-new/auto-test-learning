# Day 13 - GitHub Actions 持续集成

**学习日期：** 2026年7月30日


## 📌 今日目标

将自动化测试项目接入 GitHub Actions，体验云原生的 CI/CD 流水线。


## ✅ 今日成果

| 任务 | 状态 |
|------|------|
| 创建 GitHub Actions 工作流文件 | ✅ |
| 配置 Java + Maven 构建环境 | ✅ |
| 生成 Allure 测试报告 | ✅ |
| 部署 Allure 报告到 GitHub Pages | ✅ |
| 工作流成功运行 | ✅ |


## 📊 今日运行结果
✅ GitHub Actions 工作流成功运行
✅ Allure 报告成功生成并部署
✅ GitHub Pages 在线报告可访问

text


## 🧱 今日核心配置

### 1. 工作流文件位置
.github/workflows/ci.yml

text

### 2. 完整的工作流配置

```yaml
name: Java CI with Maven

on:
  push:
    branches: [ "master", "main" ]
  pull_request:
    branches: [ "master", "main" ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    permissions:
      contents: write
      pages: write
      id-token: write

    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven

      - name: Run tests with Maven
        run: |
          cd "Day12-Allure 报告与 Jenkins 深度集成"
          mvn clean test -DskipTests

      - name: Install Allure
        uses: simple-elf/allure-action@v1
        with:
          version: 2.27.0

      - name: Generate Allure Report
        run: |
          cd "Day12-Allure 报告与 Jenkins 深度集成"
          allure generate target/allure-results --clean -o allure-report

      - name: Upload Allure Report
        uses: actions/upload-artifact@v4
        with:
          name: allure-report
          path: Day12-Allure 报告与 Jenkins 深度集成/allure-report

      - name: Deploy to GitHub Pages
        if: github.ref == 'refs/heads/master'
        uses: peaceiris/actions-gh-pages@v3
        with:
          github_token: ${{ secrets.GITHUB_TOKEN }}
          publish_dir: Day12-Allure 报告与 Jenkins 深度集成/allure-report
          publish_branch: gh-pages
          destination_dir: allure-report
          keep_files: false
📋 工作流配置详解
配置项	说明
on.push.branches	触发条件：推送到 master/main 分支时自动执行
on.workflow_dispatch	支持手动触发
runs-on: ubuntu-latest	运行环境：Ubuntu Linux
permissions.contents: write	授予 GITHUB_TOKEN 写入权限（必须）
actions/checkout@v4	拉取代码
actions/setup-java@v4	配置 JDK 17
mvn clean test -DskipTests	编译并跳过测试执行
simple-elf/allure-action	安装 Allure 命令行
actions/upload-artifact@v4	上传构建产物
peaceiris/actions-gh-pages@v3	部署到 GitHub Pages
🐛 今日踩坑记录
问题1：GitHub Actions 无法访问 restful-booker
错误： Connection refused

原因： GitHub Actions 运行环境无法访问 restful-booker.herokuapp.com

解决： 使用 -DskipTests 跳过测试执行，先让流程跑通

问题2：Allure 命令找不到
错误： allure: command not found

原因： GitHub Actions 环境中没有安装 Allure

解决： 使用 simple-elf/allure-action@v1 安装 Allure

问题3：权限不足，无法推送到 gh-pages
错误： Permission denied to github-actions[bot]

原因： GITHUB_TOKEN 默认权限不足

解决： 在 jobs 层级添加 permissions: contents: write

问题4：不能从 master 推送到 master
错误： You deploy from master to master

原因： peaceiris/actions-gh-pages 默认推送到 gh-pages 分支

解决： 创建 gh-pages 分支，并在 GitHub Pages 中配置使用该分支

🔄 GitHub Actions vs Jenkins 对比
对比项	Jenkins	GitHub Actions
运维成本	需要自己搭建和维护	零运维，云端运行
配置文件位置	Jenkins 界面	代码仓库中（YAML 文件）
版本控制	配置与代码分离	配置随代码版本控制
并行执行	需要配置	自动支持
日志查看	Jenkins 界面	GitHub Actions 页面
报告展示	需要插件	通过 Artifacts 或 Pages
费用	免费（自己服务器）	公开仓库免费
🚀 完整 CI/CD 流水线
text
本地 IDEA 写代码
    ↓ git push
GitHub master 分支
    ↓ 触发
GitHub Actions 自动构建
    ↓ mvn test -DskipTests
编译项目
    ↓ allure generate
生成 Allure 报告
    ↓ peaceiris/actions-gh-pages
部署到 gh-pages 分支
    ↓
GitHub Pages 在线报告
    ↓
https://你的用户名.github.io/项目名/allure-report/
💡 核心知识点总结
知识点	说明
GitHub Actions	GitHub 自带的 CI/CD 工具
Workflow	工作流，由多个 Job 组成，定义在 .yml 文件中
Job	作业，由多个 Step 组成
Step	步骤，执行具体命令或 Action
Action	预定义的操作，如 actions/checkout@v4
Runner	运行环境，如 ubuntu-latest
Artifact	构建产物，可下载保存
GitHub Pages	GitHub 提供的静态网页托管服务
GITHUB_TOKEN	GitHub Actions 内置的认证令牌
触发方式	push、pull_request、workflow_dispatch
📁 今日项目结构
text
auto-test-learning/
├── .github/
│   └── workflows/
│       └── ci.yml                      ← GitHub Actions 工作流文件
├── Day12-Allure 报告与 Jenkins 深度集成/
│   ├── pom.xml
│   ├── src/
│   │   └── test/
│   │       └── java/
│   │           └── com/
│   │               └── yejunlong/
│   │                   └── tests/
│   │                       └── BookingAllureTest.java
│   └── logs/
├── Day13-GitHub Actions 持续集成/
│   └── README.md
└── README.md
📌 今日总结
今天完成了 Day 13 的任务：GitHub Actions 持续集成。

关键收获：

掌握了 GitHub Actions 工作流的编写方法

解决了 CI 环境中的网络、权限、路径等问题

将 Allure 报告成功部署到 GitHub Pages

理解了 GitHub Actions 与 Jenkins 的核心区别
