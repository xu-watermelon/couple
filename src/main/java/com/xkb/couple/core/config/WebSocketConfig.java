package com.xkb.couple.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket 配置类（支持通知和聊天功能）
 * @author xuwatermelon
 * @date 2026/02/22
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 配置消息代理，用于广播消息和点对点消息
        // 支持三种类型的消息：
        // 1. /topic - 系统广播消息
        // 2. /queue - 个人通知消息
        // 3. /chat - 聊天消息
        config.enableSimpleBroker("/topic", "/queue", "/chat");

        // 配置应用前缀，用于接收客户端消息
        config.setApplicationDestinationPrefixes("/app");

        // 配置用户前缀，用于点对点消息
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册 WebSocket 端点，允许 SockJS 回退
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}