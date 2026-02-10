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
     * 用户不存在或密码错误
     */
    PASSWORD_ERROR("1006", "密码错误"),
    /**
     * 两次密码不一致
     */
    PASSWORD_NOT_MATCH("1007", "两次密码不一致"),

    /**
     * 用户名或密码为空
*/
    USERNAME_OR_PASSWORD_EMPTY("1008", "用户名或密码为空"),
    /**
     * 用户id为空
     */
    USER_ID_EMPTY("1009", "用户id为空"),
    /**
     * 参数校验失败
     */
    PARAMS_ERROR("1010", "参数校验失败");

    private final String code;
    private final String message;
}
