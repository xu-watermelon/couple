package com.xkb.couple.core.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 错误码枚举
 *
 *  @author xuwatermelon
 *  @date 2026/02/06
 **/
@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {
    /**
     * 错误码枚举
     */
    SUCCESS("0000", "成功"),
    /**
     * 失败
     */
    FAIL("9999", "失败"),
    /**
     * 用户不存在
     */
    USER_NOT_EXIST("1001", "用户不存在"),
    /**
     * 用户已存在
     */
    USER_EXIST("1002", "用户已存在"),
    /**
     * 用户密码错误
     */
    USER_PASSWORD_ERROR("1003", "用户密码错误"),
    /**
     * 用户未登录
     */
    USER_NOT_LOGIN("1004", "用户未登录"),
    /**
     * 用户未授权
     */
    USER_NOT_AUTHORIZED("1005", "用户未授权"),

    /**
     * 旧密码和新密码一致
     */
    OLD_PASSWORD_EQUAL_NEW_PASSWORD("1006", "旧密码和新密码一致"),
    /**
     * 两次密码不一致
     */
    PASSWORD_NOT_MATCH("1007", "两次密码不一致"),


    /**
     * 用户名或密码为空
    */
    EMAIL_OR_PASSWORD_EMPTY("1008", "邮箱或密码为空"),
    /**
     * 用户id为空
     */
    USER_ID_EMPTY("1009", "用户id为空"),
    /**
     * 参数校验失败
     */
    PARAMS_ERROR("1010", "参数校验失败"),
    /**
     * 邮箱为空
     */
    EMAIL_EMPTY("1011", "邮箱为空"),
    /**
     * 邮件发送失败
     */
    MAIL_SEND_FAILED("1012", "邮件发送失败"),
    /**
     * 邮件内容为空
     */
    EMAIL_CONTENT_EMPTY("1013", "邮件内容为空"),
    /**
     * 验证码生成失败
     */
    CAPTCHA_GENERATE_FAILED("1014", "验证码生成失败"),
    /**
     * 验证码ID为空
     */
    CAPTCHA_ID_EMPTY("1015", "验证码ID为空"),
    /**
     * 验证码为空
     */
    CAPTCHA_EMPTY("1016", "验证码为空"),
     /**
     * 验证码错误
     */
    CAPTCHA_ERROR("1017", "验证码错误"),

    /**
     * 邮箱格式错误
     */
    EMAIL_FORMAT_ERROR("1018", "邮箱格式错误"),

     /**
     * 验证码已存在
     */
     CAPTCHA_EXISTS("1019", "验证码已发送,请稍后重试"),
    /**
     * 用户已绑定情侣
     */
    USER_HAS_COUPLE("1020", "用户已绑定情侣"),
     /**
     * 用户性别错误
     */
    USER_GENDER_ERROR("1021", "用户性别错误");



    private final String code;
    private final String message;
}
