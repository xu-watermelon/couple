package com.xkb.couple.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 聊天消息 DTO（为未来聊天功能准备）
 * @author xuwatermelon
 * @date 2026/02/22
 */
@Data
public class ChatMessageDTO {
    /** 消息ID */
    private Long id;
    /** 发送者ID */
    private Long fromUserId;
    /** 发送者昵称 */
    private String fromUserName;
    /** 发送者头像 */
    private String fromUserAvatar;
    /** 接收者ID（点对点聊天） */
    private Long toUserId;
    /** 聊天室ID（群组聊天） */
    private String roomId;
    /** 消息类型：text, image, voice, etc. */
    private String type;
    /** 消息内容 */
    private String content;
    /** 消息状态：sending, sent, delivered, read */
    private String status;
    /** 时间戳 */
    private LocalDateTime timestamp;



    public ChatMessageDTO() {
        this.timestamp = LocalDateTime.now();
        this.status = "sent";
    }
}