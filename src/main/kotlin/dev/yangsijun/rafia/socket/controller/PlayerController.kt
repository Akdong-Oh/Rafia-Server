package dev.yangsijun.rafia.socket.controller

import dev.yangsijun.rafia.data.enums.*
import dev.yangsijun.rafia.data.manager.EventManager
import dev.yangsijun.rafia.data.manager.JobManager
import dev.yangsijun.rafia.data.player.Player
import dev.yangsijun.rafia.domain.room.domain.Room
import dev.yangsijun.rafia.domain.room.service.RoomService
import dev.yangsijun.rafia.global.GameUtil
import dev.yangsijun.rafia.global.RoomUtil
import dev.yangsijun.rafia.socket.message.ReadyMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageHeaderAccessor

import org.springframework.messaging.simp.SimpMessageSendingOperations

import org.springframework.web.bind.annotation.RestController


@RestController
class PlayerController(
    private val sendingOperations: SimpMessageSendingOperations,
    private val roomService: RoomService,
    private val eventManager: EventManager,
    private val jobManager: JobManager
) {
    @MessageMapping("/ready")
    fun enter(message: ReadyMessage, headerAccessor: SimpMessageHeaderAccessor) {
        // TODO WebSocketEventListener 랑 로직이 비슷한 부분이 있으니 수정 시 주의
        if (message.status != SocketStatus.READY) {
            val exMessage = GameUtil.errorMessage(message, ErrorCode.C0000) // 유효하지 않은 status
            sendingOperations.convertAndSend("/topic/" + exMessage.roomId, exMessage)
        }
        val room = roomService.findById(message.roomId) // TODO 예외처리 필요함
        val player: Player = room.players.first { it.user.id == message.userId } // TODO 예외 나옴
        room.players.remove(player)
        try {
            if (message.data.userStatus == ReadyStatus.NOT_READY) {
                room.players.add(
                    Player(
                        player.user,
                        player.id,
                        player.job,
                        ReadyStatus.NOT_READY,
                        PlayerStatus.ALIVE,
                        player.interactions
                    )
                )
                roomService.save(room)
            } else if (message.data.userStatus == ReadyStatus.READY) {
                room.players.add(
                    Player(
                        player.user,
                        player.id,
                        player.job,
                        ReadyStatus.READY,
                        PlayerStatus.ALIVE,
                        player.interactions
                    )
                )
                roomService.save(room)
            }
            sendingOperations.convertAndSend("/topic/" + message.roomId, message)
            if (isStartAble(room)) {
                // TODO 게임 시작하기
                jobManager.setJob(message, room)
                eventManager.gameStart(message, room)
            }
            return
        } catch (ex : NoSuchElementException) {
            val exMessage = GameUtil.errorMessage(message, ErrorCode.C0005) // 방 안에 존재하지 않는 유저
            sendingOperations.convertAndSend("/topic/" + exMessage.roomId, exMessage)
        }
        //TODO 에러 발생
    }

    fun isStartAble(room: Room): Boolean {
        val isAllReady: Boolean = room.players.count { it.readyStatus == ReadyStatus.READY } >= room.players.size
        val canStartHeadCount: Boolean = room.players.count() >= RoomUtil.MIN_ROOM_START_PLAYER
        val isWait: Boolean = room.eventStatus == EventStatus.WAIT
        return (isAllReady and canStartHeadCount and isWait)
    }
}