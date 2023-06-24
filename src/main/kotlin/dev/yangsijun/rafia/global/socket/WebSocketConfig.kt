package dev.yangsijun.rafia.global.socket

import dev.yangsijun.rafia.socket.MyChannelInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig: WebSocketMessageBrokerConfigurer {
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*")
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry
            .setApplicationDestinationPrefixes("/pub") // Client에서 websocket 연결 할 api 앤드포인트
            .enableStompBrokerRelay("/topic") // 이 Prefix를 받아서 messageBroker가 해당 방?에 메제지 전달
//            .setSystemHeartbeatSendInterval(1000)  // 정할수는 있는데, 이거 꺼지는 기준이???
//            .setSystemHeartbeatReceiveInterval(1000)
            .setRelayHost("localhost")
            .setVirtualHost("/")
            .setRelayPort(61613)
            .setClientLogin("guest")
            .setClientPasscode("guest")
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(MyChannelInterceptor())
    }
}