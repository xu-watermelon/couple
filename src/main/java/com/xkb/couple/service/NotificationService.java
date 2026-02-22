package com.xkb.couple.service;


import com.xkb.couple.mapper.NotificationMapper;
import com.xkb.couple.pojo.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
/**
 * 通知服务
 * @author xuwatermelon
 * @date 2026/02/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService  {
    private final NotificationMapper notificationMapper;
    /**
     * 保存通知
     * @param notification 通知消息
     */
    public void saveNotification(NotificationDTO notification) {
        log.info("保存通知: {}", notification);
        // 保存通知到数据库
        notificationMapper.insert(notification);
    }
}
