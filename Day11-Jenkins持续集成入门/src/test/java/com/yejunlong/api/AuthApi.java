package com.yejunlong.api;

import com.yejunlong.client.ApiClient;
import com.yejunlong.model.AuthRequest;
import com.yejunlong.utils.JsonUtils;
import io.restassured.response.Response;

public class AuthApi {

    public static final String LOGIN_PATH = "/auth";

    public static Response login(AuthRequest request) {
        String JsonBody = JsonUtils.toJson(request);
        return ApiClient.post(LOGIN_PATH, JsonBody);
    }

    public static Response login(String username,String password){
        AuthRequest request = new AuthRequest(username,password);
        return login(request);
    }
}
