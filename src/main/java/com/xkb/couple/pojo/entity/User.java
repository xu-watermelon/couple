package com.xkb.couple.pojo.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * 用户表
 *  @author xuwatermelon
 *  @date 2026/02/06
 */
@Data
@Builder
@TableName("user")
public class User {
        /**
         * 主键 用户id
         */
        @TableId(type = IdType.AUTO)
        private Long id;

        /**
         * 昵称
         */
        @TableField("nickname")
        private String nickname;
        /**
         * 密码
         */
        @TableField("password")
        private String password;
        /**
         * 是否有情侣 0-没有 1-有
         */
        @TableField("has_couple")
        private Integer hasCouple;
        /**
          * 头像
         */
        @TableField("avatar")
        private String avatar;
        /**
         * 性别 0-女 1-男
         */
        @TableField("gender")
        private Integer gender;
        /**
         * 生日
         */
        @TableField("birthday")
        private LocalDate birthday;
        /**
         * 邮箱
         */
        @TableField("email")
        private String email;
        /**
         * 创建时间
         */
        @TableField("create_time")
        private LocalDateTime createTime;
         /**
         * 更新时间
         */
        @TableField("update_time")
        private LocalDateTime updateTime;

}
