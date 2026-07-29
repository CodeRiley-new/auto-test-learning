package com.yejunlong.utils;

import io.restassured.response.Response;
import org.testng.Assert;

public class AssertUtils {

    //断言请求成功
    public static void assertSuccess(Response response){
        Assert.assertEquals(
                response.getStatusCode(),
                200,
                "请求失败，状态码:"+response.getStatusCode()+"响应体"+response.getBody().asString()
        );
    }

    //断言token有效
    public static void assertTokenValid(String token){
        Assert.assertNotNull(token,"token不应为空");
        Assert.assertNotEquals(token,"","token不应为空字符串");
    }

    //断言指定状态码
    public static void assertStatusCode(Response response,int code){
        Assert.assertEquals(
                response.getStatusCode(),
                code,
                "请求失败，状态码:"+response.getStatusCode()+"响应体"+response.getBody().asString()
        );
    }

    //断言响应体包含某个字段
    public static void assertFieldExist(Response response,String jsonPath){
        Object value = response.getBody().jsonPath().get(jsonPath);
        Assert.assertNotNull(value,"字段："+jsonPath+",不存在或为空");
    }
}
