package com.yejunlong.api;

import com.yejunlong.client.ApiClient;
import com.yejunlong.model.BookingRequest;
import com.yejunlong.utils.JsonUtils;
import io.restassured.response.Response;

public class BookingApi {

    private static String BOOKING_PATH = "/booking";

    //创建订单
    public static Response createBooking(BookingRequest request){
        String JsonBody = JsonUtils.toJson(request);
        return ApiClient.post(BOOKING_PATH,JsonBody);
    }

    //查询订单
    public static Response getBooking(String token,int bookingId){
        return ApiClient.getWithToken(BOOKING_PATH+"/"+bookingId,token);
    }
}
