package com.xkb.couple.core.utils;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * 日志脱敏工具类
 * 用于对日志中的敏感信息进行脱敏处理
 *
 * @author xuwatermelon
 * @date 2026-02-07
 */
public class LogDesensitizeUtil {

    /**
     * 密码脱敏：全部用*替代
     */
    public static String desensitizePassword(String password) {
        if (StringUtils.isBlank(password)) {
            return password;
        }
        return "*".repeat(password.length());
    }

    /**
     * 昵称脱敏：保留首尾字符，中间用*替代
     * 示例：zhangsan -> z******n
     */
    public static String desensitizeNickname(String nickname) {
        if (StringUtils.isBlank(nickname) || nickname.length() <= 2) {
            return nickname;
        }
        int maskLength = nickname.length() - 2;
        return nickname.charAt(0) + "*".repeat(maskLength) + nickname.charAt(nickname.length() - 1);
    }

    /**
     * 手机号脱敏：保留前3后4位，中间用*替代
     * 示例：13800138000 -> 138****8000
     */
    public static String desensitizeUsername(String phone) {
        if (StringUtils.isBlank(phone) || phone.length() < 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
    }

    /**
     * 邮箱脱敏：用户名部分保留首尾字符，域名部分保留
     * 示例：zhangsan@example.com -> z******n@example.com
     */
    public static String desensitizeEmail(String email) {
        if (StringUtils.isBlank(email) || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return email;
        }
        String username = desensitizeUsername(parts[0]);
        return username + "@" + parts[1];
    }

        /**
         * 处理token脱敏：保留首尾字符，中间用*替代
         * 示例：eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE1MTYyMzkwMjJ9.1234567890abcdef1234567890abcdef -> e******************************f
         */
        public static String desensitizeToken (String token){
            if (StringUtils.isBlank(token)) {
                return token;
            }
            int startIndex = token.indexOf(".") + 1;
            int endIndex = token.lastIndexOf(".");
            if (startIndex >= endIndex) {
                return token;
            }
            return token.substring(0, startIndex) + "*".repeat(endIndex - startIndex) + token.substring(endIndex);
        }
    }
