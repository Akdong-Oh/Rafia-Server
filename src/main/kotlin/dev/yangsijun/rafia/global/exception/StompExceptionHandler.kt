//package dev.yangsijun.rafia.global.exception
//
//import org.springframework.messaging.Message
//import org.springframework.stereotype.Component
//import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler
//
//
//@Component
//class StompExceptionHandler : StompSubProtocolErrorHandler() {
//    override fun handleClientMessageProcessingError(
//        clientMessage: Message<ByteArray>?,
//        ex: Throwable
//    ): Message<ByteArray>? {
//        return super.handleClientMessageProcessingError(clientMessage, ex)
//    }
//}