package dev.yangsijun.rafia.global.socket

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig: WebSocketMessageBrokerConfigurer {
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*")
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry
            .setApplicationDestinationPrefixes("/pub") // Client에서 websocket 연결 할 api 앤드포인트
            .enableStompBrokerRelay("/topic") // 이 Prefix를 받아서 messageBroker가 해당 방?에 메제지 전달
            .setRelayHost("localhost")
            .setVirtualHost("/")
            .setRelayPort(5672)
            .setClientLogin("guest")
            .setClientPasscode("guest")
            //.setHeartbeatValue()
    }
}