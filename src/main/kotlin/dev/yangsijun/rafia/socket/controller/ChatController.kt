package dev.yangsijun.rafia.socket.controller

import dev.yangsijun.rafia.data.enums.ErrorCode
import dev.yangsijun.rafia.data.enums.SocketStatus
import dev.yangsijun.rafia.domain.room.service.RoomService
import dev.yangsijun.rafia.global.GameUtil
import dev.yangsijun.rafia.socket.message.ChatMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageHeaderAccessor

import org.springframework.messaging.simp.SimpMessageSendingOperations

import org.springframework.web.bind.annotation.RestController


@RestController
class ChatController(
    private val sendingOperations: SimpMessageSendingOperations,
    private val roomService: RoomService
) {
    @MessageMapping("/chat")
    fun enter(message: ChatMessage, headerAccessor: SimpMessageHeaderAccessor) {
        // TODO WebSocketEventListener 랑 로직이 비슷한 부분이 있으니 수정 시 주의
        if (message.status != SocketStatus.CHAT) {
            val exMessage = GameUtil.errorMessage(message, ErrorCode.C0000) // 유효하지 않은 status
            sendingOperations.convertAndSend("/topic/" + exMessage.roomId, exMessage)
        }
        try {
            val room = roomService.findById(message.roomId) // TODO 예외처리 필요함
            room.players.first { it.user.id == message.userId } // TODO 예외 나옴 + 유저 있는지 확인하기 위함
            sendingOperations.convertAndSend("/topic/" + message.roomId, message)
            return
        } catch (ex: NoSuchElementException) {
            val exMessage = GameUtil.errorMessage(message, ErrorCode.C0005) // 방 안에 존재하지 않는 유저
            sendingOperations.convertAndSend("/topic/" + exMessage.roomId, exMessage)
        }
        //TODO 에러 발생 + 여기 있으면 안됨
    }
}