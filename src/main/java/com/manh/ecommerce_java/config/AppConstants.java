package com.manh.ecommerce_java.config;

public class AppConstants {
    public static final String PAGE_NUMBER = "0";
    public static final String PAGE_SIZE = "2";
    public static final String SORT_BY = "id";
    public static final String SORT_DIR = "asc";
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    public static final String[] PUBLIC_URLS = {"/v3/api-docs/**", "/swagger-ui/**",
            "/register/**", "/login", "/**"};
    public static final String[] USER_URLS = {};
    public static final String[] ADMIN_URLS = {"/api/admin/**"};
}
