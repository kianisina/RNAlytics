package com.sp.web.security;

public class SecurityConstant {
// Must be at least 32 characters long for HMAC-SHA256!
    public static final String SECRET_KEY = "ThisIsAVerySecureSuperSecretKeyForMyVueApp!"; 
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
