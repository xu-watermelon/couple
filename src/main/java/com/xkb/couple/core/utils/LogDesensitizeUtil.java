package com.xkb.couple.core.utils;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

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
     * 邮箱脱敏：用户名部分保留首尾字符，域名部分保留
     * 示例：zhangsan@example.com -> z******n@example.com
     */
    public static String desensitizeEmail(String email) {
        // 参数验证
        if (StringUtils.isBlank(email) || !email.contains("@")) {
            return email;
        }

        String[] parts = email.split("@");
        if (parts.length != 2) {
            return email;
        }

        String username = parts[0];
        String domain = parts[1];

        // 处理不同长度的用户名
        if (username.isEmpty()) {
            return email;
        } else if (username.length() == 1) {
            // 单字符用户名：*@example.com
            return "*@" + domain;
        } else if (username.length() == 2) {
            // 双字符用户名：ab@example.com（完整保留）
            return username + "@" + domain;
        } else {
            // 多字符用户名：a***z@example.com
            char firstChar = username.charAt(0);
            char lastChar = username.charAt(username.length() - 1);
            int maskLength = username.length() - 2;
            String mask = "*".repeat(maskLength);
            return firstChar + mask + lastChar + "@" + domain;
        }
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

        /**
         * 验证码脱敏：全部用*替代
         */
             public static Object desensitizeCaptcha(@NotBlank(message = "验证码不能为空") @Pattern(regexp = "^\\d{6}$", message = "验证码格式不正确") String captcha) {
                 return "*".repeat(captcha.length());
            }
}
