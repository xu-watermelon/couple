package com.xkb.couple.core.common.constants;

public class SystemConfigConstans {
    /**
     * token
     */
    public static final String TOKEN = "token";
    /**
     * jwt密钥
     */
    public static final String JWT_SECRET = "couple_jwt_secret";
    /**
     * http请求头中的Authorization字段
     */
    public static final String HTTP_AUTHORIZATION = "Authorization";
    /**
     * token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";
    /**
     * Token脱敏长度：日志中只显示Token的前N个字符
     */
    public static final int TOKEN_MASK_LENGTH = 20;
}
