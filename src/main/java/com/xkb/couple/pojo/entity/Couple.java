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
 * 情侣表
 * @author xuwatermelon
 * @date 2026/02/18
 */
@Data
@Builder
@TableName("couple")
public class Couple {
    /**
     * 主键 情侣id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 恋爱开始日期
     */
    @TableField("love_start_date")
    private LocalDate loveStartDate;

    /**
     * 情侣名称
     */
    @TableField("couple_name")
    private String coupleName;

    /**
     * 男方用户id
     */
    @TableField("male_user_id")
    private Long maleUserId;

    /**
     * 女方用户id
     */
    @TableField("female_user_id")
    private Long femaleUserId;

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