package com.kjh.boardback.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket                    // webSocket 활성화
@EnableWebSocketMessageBroker       // stomp 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {		// WebSocketMessageBrokerConfigurer 인터페이스를 구현

    // 1. Stomp websocket 연결
    @Override
    public void  registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")             /// ws://example.com/ws-stomp
                .setAllowedOriginPatterns("*");
    }

    // Stomp 사용을 위한 Message Broker 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");				// 2.
        registry.setApplicationDestinationPrefixes("pub");	// 3.
    }
}
