package dev.yangsijun.rafia.socket.controller

import dev.yangsijun.rafia.data.enums.GameStatus
import dev.yangsijun.rafia.data.player.Player
import dev.yangsijun.rafia.domain.room.enums.RoomStatus
import dev.yangsijun.rafia.domain.room.service.RoomService
import dev.yangsijun.rafia.domain.user.domain.User
import dev.yangsijun.rafia.global.Util
import dev.yangsijun.rafia.socket.message.EntryMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageHeaderAccessor

import org.springframework.messaging.simp.SimpMessageSendingOperations

import org.springframework.web.bind.annotation.RestController


@RestController
class EntryController(
    private val sendingOperations: SimpMessageSendingOperations,
    private val roomService: RoomService
) {
    @MessageMapping("/entry")
    fun enter(message: EntryMessage, headerAccessor: SimpMessageHeaderAccessor) {
        if (message.status != GameStatus.ENTRY)
            throw IllegalArgumentException("유효하지 않은 status")
        val room = roomService.findById(message.roomId) // TODO 예외처리 필요함
        if (room.status == RoomStatus.PLAY)
            throw IllegalStateException("플레이 중")
        else if (room.players.count() >= Util.MAX_ROOM_PLAYER)
            throw IllegalStateException("인원수 많음")
        else if (room.players.any { it.user.id == message.data.userId })
            throw IllegalArgumentException("이미 있는 유저")
        else {
            val user = User(message.data.userId, message.data.userName)
            room.players.add(Player(user, headerAccessor.sessionId!!, null, listOf()))
            val savedRoom = roomService.save(room)
            if (savedRoom.players.count() > Util.MAX_ROOM_PLAYER) // 9 이상 - 나 포함하고도 1명더 들어옴
                throw IllegalStateException("인원수 많음 / 롤백") // 롤백
        }
        sendingOperations.convertAndSend("/topic/" + message.roomId, message)
    }
}