package com.xkb.couple.pojo.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
/**
 * 用户VO
 * @author xuwatermelon
 * @date 2026/02/07
 */
@Data
@Builder
public class UserVO {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nickname;

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
    private LocalDate birthday;



}
