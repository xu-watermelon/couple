package com.xkb.couple.pojo.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class UserVO {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 用户名
     */
    private String username;
    /**
     * 是否有情侣 0-没有 1-有
     */
    private Integer hasCouple;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 性别 0-女 1-男
     */
    private Integer gender;
    /**
     * 生日
     */
    private LocalDateTime birthday;


}
