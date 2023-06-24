package dev.yangsijun.rafia.socket.controller

import dev.yangsijun.rafia.data.enums.GameStatus
import dev.yangsijun.rafia.data.player.Player
import dev.yangsijun.rafia.domain.room.enums.RoomStatus
import dev.yangsijun.rafia.domain.room.service.RoomService
import dev.yangsijun.rafia.domain.user.domain.User
import dev.yangsijun.rafia.global.Util
import dev.yangsijun.rafia.socket.message.EntryMessage
import dev.yangsijun.rafia.socket.message.ExitMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageHeaderAccessor

import org.springframework.messaging.simp.SimpMessageSendingOperations

import org.springframework.web.bind.annotation.RestController


@RestController
class ExitController(
    private val sendingOperations: SimpMessageSendingOperations,
    private val roomService: RoomService
) {
    @MessageMapping("/exit")
    fun enter(message: ExitMessage, headerAccessor: SimpMessageHeaderAccessor) {
        // TODO WebSocketEventListener 랑 로직이 비슷한 부분이 있으니 수정 시 주의
        if (message.status != GameStatus.EXIT)
            throw IllegalArgumentException("유효하지 않은 status")
        val room = roomService.findById(message.roomId) // TODO 예외처리 필요함
        if (room.players.count() <= 1)
            roomService.deleteById(message.roomId)
        else {
            try {
                val player: Player = room.players.first { it.user.id == message.data.userId } // TODO 예외 나옴
                room.players.remove(player)
                roomService.save(room)
            } catch (ex : Exception) {
                throw IllegalArgumentException("방 안에 존재하지 않는 유저")
            }
        }
        sendingOperations.convertAndSend("/topic/" + message.roomId, message)
    }
}