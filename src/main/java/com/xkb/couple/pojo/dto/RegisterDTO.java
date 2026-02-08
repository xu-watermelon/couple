package com.xkb.couple.pojo.dto;

import lombok.Data;
/**
 * 注册参数 DTO
 * @author xuwatermelon
 * @date 2026/02/08
 */
@Data
public class RegisterDTO {
    private String username;
    private String password;
    private String confirmPassword;
}