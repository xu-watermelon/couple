package com.xkb.couple.service;

import com.xkb.couple.pojo.dto.ChatMessageDTO;
import com.xkb.couple.pojo.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * WebSocket 消息发送服务
 * 支持通知和聊天消息
 * @author xuwatermelon
 * @date 2026/02/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketService {
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 发送个人通知
     *
     * @param notification 通知消息
     */
    public void sendNotification(NotificationDTO notification) {
        try {
            messagingTemplate.convertAndSendToUser(
                    notification.getReceiverId().toString(),
                    "/queue/notifications",
                    notification
            );
            log.info("WebSocket 通知发送成功，发送用户ID：{},接收用户ID：{},通知类型：{}", notification.getSenderId(), notification.getReceiverId(), notification.getType());
        } catch (Exception e) {
            log.warn("WebSocket 通知发送失败，接收用户可能离线，发送用户ID：{},接收用户ID：{},错误信息：{}", notification.getSenderId(), notification.getReceiverId(), e.getMessage());
            notificationService.saveNotification(notification);
            log.info("通知已保存到数据库，通知ID：{}", notification.getId());
        }
    }

    /**
     * 发送系统广播
     * @param notification 通知消息
     */
    public void sendBroadcast(NotificationDTO notification) {
        try {
            messagingTemplate.convertAndSend(
                    "/topic/system",
                    notification
            );
            log.info("WebSocket 系统广播发送成功，通知类型：{}", notification.getType());
        } catch (Exception e) {
            log.error("WebSocket 系统广播发送失败，错误信息：{}", e.getMessage(), e);
        }
    }

    /**
     * 发送点对点聊天消息
     * @param fromUserId 发送者用户ID
     * @param toUserId 接收者用户ID
     * @param message 聊天消息
     * @return 发送是否成功
     */
    public boolean sendChatMessage(Long fromUserId, Long toUserId, ChatMessageDTO message) {
        try {
            messagingTemplate.convertAndSendToUser(
                    toUserId.toString(),
                    "/queue/chat",
                    message
            );
            log.debug("WebSocket 聊天消息发送成功，发送者：{}，接收者：{}", fromUserId, toUserId);
            return true;
        } catch (Exception e) {
            log.warn("WebSocket 聊天消息发送失败，接收者可能离线，接收者ID：{}，错误信息：{}", toUserId, e.getMessage());
            return false;
        }
    }

    /**
     * 发送情侣聊天消息
     * @param coupleId 情侣ID
     * @param message 聊天消息
     */
    public void sendCoupleChatMessage(Long coupleId, ChatMessageDTO message) {
        try {
            messagingTemplate.convertAndSend(
                    "/chat/couple/" + coupleId,
                    message
            );
            log.debug("WebSocket 情侣聊天消息发送成功，情侣ID：{}", coupleId);
        } catch (Exception e) {
            log.error("WebSocket 情侣聊天消息发送失败，情侣ID：{}，错误信息：{}", coupleId, e.getMessage(), e);
        }
    }

    /**
     * 发送群组聊天消息
     * @param roomId 房间ID
     * @param message 聊天消息
     */
    public void sendGroupChatMessage(String roomId, ChatMessageDTO message) {
        try {
            messagingTemplate.convertAndSend(
                    "/chat/room/" + roomId,
                    message
            );
            log.debug("WebSocket 群组聊天消息发送成功，房间ID：{}", roomId);
        } catch (Exception e) {
            log.error("WebSocket 群组聊天消息发送失败，房间ID：{}，错误信息：{}", roomId, e.getMessage(), e);
        }
    }


}