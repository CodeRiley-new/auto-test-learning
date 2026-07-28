package com.yejunlong.config;

/**
 * 统一配置类
 */
public class ApiConfig {

        public static final String ENV = "test";
        //基础URL
        public static final String BASE_URL_TEST = "https://restful-booker.herokuapp.com";
        public static final String BASE_URL_PROD = "https://restful-booker.herokuapp.com";
        //代理配置
        public static final String PROXY_HOST = "127.0.0.1";
        public static final int PROXY_PORT = 7897;
        //测试数据
        public static final String TEST_USERNAME = "admin";
        public static final String TEST_PASSWORD = "password123";

        public static String getBaseUrl() {
            if ("prod".equalsIgnoreCase(ENV)) {
                return BASE_URL_PROD;
            }
            return BASE_URL_TEST;
        }
}
