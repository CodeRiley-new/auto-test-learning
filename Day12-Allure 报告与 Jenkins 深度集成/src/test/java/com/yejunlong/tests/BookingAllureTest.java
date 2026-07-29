package com.yejunlong.tests;

import com.yejunlong.api.AuthApi;
import com.yejunlong.api.BookingApi;
import com.yejunlong.config.ApiConfig;
import com.yejunlong.model.AuthResponse;
import com.yejunlong.model.BookingRequest;
import com.yejunlong.model.BookingResponse;
import com.yejunlong.utils.AssertUtils;
import com.yejunlong.utils.JsonUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;


@Epic("自动化测试框架")
@Feature("订单管理")
public class BookingAllureTest {

    //创建日志对象
    private static final Logger log = LoggerFactory.getLogger(BookingAllureTest.class);
    private static String authToken ;
    private static int bookingId;

    /**
     * 登入测试
     * 用日志记录登入信息
     */
    @Test
    @Description("验证登入功能是否正常")
    @Feature("登入测试")
    public void testLogin(){
        log.info("========== 开始测试：登录 ==========");

        Response response = AuthApi.login(ApiConfig.TEST_USERNAME,ApiConfig.TEST_PASSWORD);
        AssertUtils.assertSuccess(response);
        //获取token
        AuthResponse authResponse = JsonUtils.fromJson(response.getBody().asString(),AuthResponse.class);
        authToken = authResponse.getToken();
        AssertUtils.assertTokenValid(authToken);

        log.info("登录成功，token:{}",authToken);
        log.info("========== 登录测试完成 ==========");
    }

    /**
     * 创建订单测试
     * 用日志记录订单信息
     */
    @Test(dependsOnMethods = "testLogin")
    @Description("验证创建订单功能是否正常")
    @Feature("创建订单测试")
    public void testCreateBooking(){
        log.info("========== 开始测试：创建订单 ==========");

        BookingRequest request = new BookingRequest();
        request.setFirstname("张三");
        request.setLastname("测试");
        request.setTotalprice(150);
        request.setDepositpaid(true);

        BookingRequest.BookingDates dates = new BookingRequest.BookingDates();
        dates.setCheckin("2026-07-26");
        dates.setCheckout("2026-07-28");
        request.setBookingdates(dates);
        request.setAdditionalneeds("日志测试");

        log.debug("请求数据：{}",JsonUtils.toJson(request));
        Response response = BookingApi.createBooking(request);
        AssertUtils.assertSuccess(response);

        BookingResponse bookingResponse = JsonUtils.fromJson(response.getBody().asString(),BookingResponse.class);
        bookingId = bookingResponse.getBookingid();
        AssertUtils.assertFieldExist(response,"bookingid");

        log.info("创建订单成功，订单号：{}",bookingId);
        log.info("========== 创建订单测试完成 ==========");
    }

    /**
     * 查询订单测试
     * 用日志记录订单信息
     */
    @Test(dependsOnMethods = {"testLogin","testCreateBooking"})
    @Description("验证查询订单功能是否正常")
    @Feature("查询订单测试")
    public void testGetBooking(){
        log.info("========== 开始测试：查询订单 ==========");

        Response response  =  BookingApi.getBooking(authToken,bookingId);
        AssertUtils.assertSuccess(response);

        String firstname = response.getBody().jsonPath().get("firstname");
        Assert.assertEquals(firstname,"张三","订单信息不匹配");

        log.info("查询订单成功，订单号：{}",bookingId);
        log.info("========== 查询订单测试完成 ==========");
    }
}
