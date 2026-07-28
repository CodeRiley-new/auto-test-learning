# Day 01 - 自动化测试入门（SauceDemo 实战）

**学习日期：** 2026年7月15日

---

## 📌 今日学习目标

- [x] 了解自动化测试的基本概念
- [x] 搭建 Selenium + Java + TestNG 环境
- [x] 掌握 3 种元素定位方式（id、cssSelector、xpath）
- [x] 学会显式等待（WebDriverWait）
- [x] 掌握 TestNG 断言（Assert）
- [x] 完成 3 个真实测试用例

---

## 🛠️ 环境配置

| 工具 | 版本 |
|------|------|
| JDK | 21.0.10 |
| Selenium | 4.20.0 |
| TestNG | 7.8.0 |
| EdgeDriver | 150.0.4078.65 |
| IDE | IntelliJ IDEA 2025.3.1 |

---

## 🎯 测试网站

**SauceDemo** - 专为自动化测试设计的演示网站

- 网址：https://www.saucedemo.com/
- 测试账号：`standard_user` / `secret_sauce`
- 特点：无任何反爬机制，非常适合初学者练习

---

## 📝 完成的测试用例

| 测试用例 | 验证内容 | 状态 |
|----------|----------|------|
| testLoginSuccess | 使用正确账号登录成功 | ✅ 通过 |
| testLoginFailed | 使用错误密码显示错误提示 | ✅ 通过 |
| testAddToCart | 登录后将商品加入购物车 | ✅ 通过 |

---

##实现代码

package com.yejunlong.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;

public class SauceDemoTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.edge.driver", "drivers/msedgedriver.exe");

        EdgeOptions options = new EdgeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");

        driver = new EdgeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * 测试用例1：验证登录功能（正确账号）
     * 场景：输入正确用户名和密码，点击登录，验证是否进入商品列表页
     */
    @Test
    public void testLoginSuccess() {
        // 1. 打开 SauceDemo 首页
        driver.get("https://www.saucedemo.com/");

        // 2. 输入用户名
        driver.findElement(By.id("user-name")).sendKeys("standard_user");

        // 3. 输入密码
        driver.findElement(By.id("password")).sendKeys("secret_sauce");

        // 4. 点击登录按钮
        driver.findElement(By.id("login-button")).click();

        // 5. 等待商品列表容器出现，验证登录成功
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inventory_container")));
        boolean isInventoryDisplayed = driver.findElement(By.id("inventory_container")).isDisplayed();

        Assert.assertTrue(isInventoryDisplayed, "登录失败，未进入商品列表页");
        System.out.println("登录成功！已进入商品列表页");
    }

    /**
     * 测试用例2：验证登录失败（错误密码）
     * 场景：输入错误密码，验证是否显示错误提示
     */
    @Test
    public void testLoginFailed() {
        driver.get("https://www.saucedemo.com/");

        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("wrong_password");
        driver.findElement(By.id("login-button")).click();

        // 等待错误提示出现
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        String errorMsg = driver.findElement(By.cssSelector("[data-test='error']")).getText();

        System.out.println("错误提示：" + errorMsg);
        Assert.assertTrue(errorMsg.contains("Username and password do not match"), "错误提示内容不正确");
    }

    /**
     * 测试用例3：验证加入购物车功能
     * 场景：登录后，将第一个商品加入购物车，验证购物车角标变为 1
     */
    @Test
    public void testAddToCart() {
        // 第一步：登录
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inventory_container")));

        // 第二步：找到第一个商品的"Add to cart"按钮并点击
        // 用 XPath 定位第一个商品的按钮（更稳定）
        WebElement firstAddButton = driver.findElement(By.xpath("//div[@class='inventory_item'][1]//button"));
        firstAddButton.click();

        // 第三步：验证购物车角标从无变为 "1"
        String cartBadge = driver.findElement(By.className("shopping_cart_badge")).getText();
        System.out.println("🛒 购物车角标：" + cartBadge);
        Assert.assertEquals(cartBadge, "1", "加入商品后购物车角标应为1");
        System.out.println("商品已成功加入购物车");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("浏览器已关闭");
        }
    }
}

## 💻 核心代码示例

###1. 元素定位（3种方式）

// 方式1：By.id —— 最推荐（快速、稳定）
driver.findElement(By.id("user-name"));

// 方式2：By.cssSelector —— 灵活
driver.findElement(By.cssSelector("[data-test='error']"));

// 方式3：By.xpath —— 万能但脆弱
driver.findElement(By.xpath("//div[@class='inventory_item'][1]//button"));

###2. 显式等待

WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inventory_container")));

###3. TestNG 断言

Assert.assertTrue(condition, "失败时的提示信息");
Assert.assertEquals(actual, expected, "失败时的提示信息");

##🔄 测试执行流程

@BeforeMethod → 打开浏览器
    │
    ▼
@Test → 执行测试用例
    │
    ▼
@AfterMethod → 关闭浏览器
    │
    ▼
（重复执行下一个测试用例）

##📊 运行结果

===============================================
Default Suite
Total tests run: 3, Passes: 3, Failures: 0, Skips: 0
===============================================
✅ 所有测试用例全部通过！

##🧠 今日收获总结

1. 为什么从百度换到 SauceDemo？
对比项	百度	SauceDemo
反爬机制	有，会拦截自动化操作	无，专为测试设计
代码稳定性	经常失效	稳定运行
学习价值	学会对抗反爬	学会真正的测试技能
2. 三种定位方式对比
定位方式	优点	缺点	优先级
By.id	最快、最稳定	需要元素有 id	⭐⭐⭐⭐⭐ 首选
By.name	比较稳定	需要元素有 name	⭐⭐⭐⭐ 次选
By.cssSelector	灵活、速度快	语法稍复杂	⭐⭐⭐ 第三选择
By.xpath	万能	速度慢、易失效	⭐⭐ 最后选择
3. 面试题准备
Q：你最喜欢用哪种定位方式？为什么？

A：优先用 By.id，因为 id 通常是唯一的，定位最快最稳定。如果元素没有 id，我会用 CSS 选择器。XPath 虽然万能，但维护成本高，页面结构一变化就容易失效。

##📁 项目结构

auto-test-day1/
├── Day01-SauceDemo/
│   ├── README.md                    # 学习总结（本文件）
│   └── SauceDemoTest.java           # 测试代码
├── drivers/
│   └── msedgedriver.exe             # Edge 驱动
└── pom.xml                          # Maven 配置

##📚 参考资料

