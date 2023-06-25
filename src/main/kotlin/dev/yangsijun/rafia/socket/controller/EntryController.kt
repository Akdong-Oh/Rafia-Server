package dev.yangsijun.rafia.socket.controller

import dev.yangsijun.rafia.data.enums.EventStatus
import dev.yangsijun.rafia.data.enums.SocketStatus
import dev.yangsijun.rafia.data.enums.PlayerStatus
import dev.yangsijun.rafia.data.player.Player

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
        if (message.status != SocketStatus.ENTRY)
            throw IllegalArgumentException("유효하지 않은 status")
        val room = roomService.findById(message.roomId) // TODO 예외처리 필요함
        if (room.eventStatus != EventStatus.WAIT)
            throw IllegalStateException("플레이 중")
        else if (room.players.count() >= Util.MAX_ROOM_PLAYER)
            throw IllegalStateException("인원수 많음")
        else if (room.players.any { it.user.id == message.userId })
            throw IllegalArgumentException("이미 있는 유저")
        else {
            val user = User(message.userId, message.data.userName)
            room.players.add(Player(user, headerAccessor.sessionId!!, null, PlayerStatus.NOT_READY, mutableMapOf()))
            val savedRoom = roomService.save(room)
            if (savedRoom.players.count() > Util.MAX_ROOM_PLAYER) // 9 이상 - 나 포함하고도 1명더 들어옴
                throw IllegalStateException("인원수 많음 / 롤백") // 롤백
            sendingOperations.convertAndSend("/topic/" + message.roomId, message)
            return
        }
        //TODO 에러 발생
    }
}