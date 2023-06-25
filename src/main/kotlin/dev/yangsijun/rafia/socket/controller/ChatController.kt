package dev.yangsijun.rafia.socket.controller

import dev.yangsijun.rafia.data.enums.GameStatus
import dev.yangsijun.rafia.data.enums.PlayerStatus
import dev.yangsijun.rafia.data.player.Player
import dev.yangsijun.rafia.domain.room.domain.Room
import dev.yangsijun.rafia.domain.room.service.RoomService
import dev.yangsijun.rafia.global.Util
import dev.yangsijun.rafia.socket.message.ChatMessage
import dev.yangsijun.rafia.socket.message.ReadyMessage
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
        if (message.status != GameStatus.CHAT)
            throw IllegalArgumentException("유효하지 않은 status")
        val room = roomService.findById(message.roomId) // TODO 예외처리 필요함
        room.players.first { it.user.id == message.userId } // TODO 예외 나옴 + 유저 있는지 확인하기 위함
        try {
            sendingOperations.convertAndSend("/topic/" + message.roomId, message)
        } catch (ex: NoSuchElementException) {
            throw IllegalArgumentException("방 안에 존재하지 않는 유저")
        }
        //TODO 에러 발생 + 여기 있으면 안됨
    }

    fun isStartAble(room: Room): Boolean {
        val isAllReady: Boolean = room.players.count { it.status == PlayerStatus.READY } == room.players.size
        val canStartHeadCount: Boolean = room.players.count() >= Util.MIN_ROOM_START_PLAYER
        return (isAllReady and canStartHeadCount)
    }
}