package dev.yangsijun.rafia.socket.controller

import dev.yangsijun.rafia.data.enums.*
import dev.yangsijun.rafia.data.player.Player

import dev.yangsijun.rafia.domain.room.service.RoomService
import dev.yangsijun.rafia.domain.user.domain.User
import dev.yangsijun.rafia.global.GameUtil
import dev.yangsijun.rafia.global.RoomUtil
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
        if (message.status != SocketStatus.ENTRY) {
            val exMessage = GameUtil.errorMessage(message, ErrorCode.C0000) // 유효하지 않은 status
            sendingOperations.convertAndSend("/topic/" + exMessage.roomId, exMessage)
        }
        val room = roomService.findById(message.roomId)
        if (room.eventStatus != EventStatus.WAIT) {
            val exMessage = GameUtil.errorMessage(message, ErrorCode.C0003) // 플레이 중
            sendingOperations.convertAndSend("/topic/" + exMessage.roomId, exMessage)
        } else if (room.players.count() >= RoomUtil.MAX_ROOM_PLAYER) {
            val exMessage = GameUtil.errorMessage(message, ErrorCode.C0001)  // 인원수 많음
            sendingOperations.convertAndSend("/topic/" + exMessage.roomId, exMessage)
        } else if (room.players.any { it.user.id == message.userId }) {
            val exMessage = GameUtil.errorMessage(message, ErrorCode.C0004) // 이미 있는 유저
            sendingOperations.convertAndSend("/topic/" + exMessage.roomId, exMessage)
        } else {
            val user = User(message.userId, message.data.userName)
            room.players.add(
                    Player(
                            user,
                            headerAccessor.sessionId!!,
                            null,
                            ReadyStatus.NOT_READY,
                            PlayerStatus.ALIVE,
                            mutableMapOf()
                    )
            )
            val savedRoom = roomService.save(room)
            if (savedRoom.players.count() > RoomUtil.MAX_ROOM_PLAYER) { // 9 이상 - 나 포함하고도 1명 더 들어옴
                val exMessage = GameUtil.errorMessage(message, ErrorCode.C0002) // 인원수 많음 / 롤백
                sendingOperations.convertAndSend("/topic/" + exMessage.roomId, exMessage)
            }
            sendingOperations.convertAndSend("/topic/" + message.roomId, message)
        }
    }
}