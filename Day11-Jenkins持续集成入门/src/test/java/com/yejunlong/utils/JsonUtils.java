package com.yejunlong.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json工具类
 * 用于对象转json和json转对象
 */
public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    //对象转json
    public static String toJson(Object obj) {
        try{
            return mapper.writeValueAsString(obj);
        }catch (Exception e){
            throw new RuntimeException("对象转json失败"+e.getMessage(),e);
        }
    }

    //json转对象
    public static <T> T fromJson(String json,Class<T> clazz){
        try{
            return mapper.readValue(json,clazz);
        }catch (Exception e){
            throw new RuntimeException("json转对象失败"+e.getMessage(),e);
        }
    }
}
