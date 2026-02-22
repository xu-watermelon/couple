package com.xkb.couple.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知消息 DTO
 * @author xuwatermelon
 * @date 2026/02/22
 */
@Data
@Builder
@TableName("notification")
public class NotificationDTO {
    /** 通知ID */
    private Long id;
    /** 发送者用户ID */
    private Long senderId;
    /** 接收者用户ID */
    private Long receiverId;
    /** 消息类型：couple_request, system, etc. */
    private String type;
    /** 消息标题 */
    private String title;
    /** 消息内容 */
    private String content;
    /** 关联ID */
    private Long relatedId;
    /** 通知状态：状态：0-未读, 1-已读, 2-已处理 */
    private String status;
    /** 时间戳 */
    private LocalDateTime timestamp;
    /** 创建时间 */
    private LocalDateTime createTime;


}