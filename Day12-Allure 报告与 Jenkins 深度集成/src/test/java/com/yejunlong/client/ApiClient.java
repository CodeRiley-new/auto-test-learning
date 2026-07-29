package com.yejunlong.client;

import com.yejunlong.config.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
/**
 * 请求客户端类
 */
public class ApiClient {

    static {
        RestAssured.baseURI = ApiConfig.getBaseUrl();
        RestAssured.proxy(ApiConfig.PROXY_HOST, ApiConfig.PROXY_PORT);
        RestAssured.useRelaxedHTTPSValidation();
    }

    //post请求
    public static Response post(String path, String body) {
        return given()
                .contentType(ContentType.JSON)
                .log().all()
                .body(body)
        .when()
                .post(path)
        .then()
                .log().all()
                .extract().response();
    }

    //get(带Token)请求
    public static Response getWithToken(String path, String token) {
        return given()
                .header("Cookie","token="+token)
                .log().all()
        .when()
                .get(path)
        .then()
                .log().all()
                .extract().response();
    }
}
