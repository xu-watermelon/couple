package com.xkb.couple.pojo.dto;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    /**
     * 旧密码
     */
    private String oldPassword;
    /**
     * 新密码
     */
    private String newPassword;
    /**
     * 确认新密码
     */
    private String confirmNewPassword;
}
