package dev.yangsijun.rafia.socket.controller

import dev.yangsijun.rafia.data.enums.EventStatus
import dev.yangsijun.rafia.data.enums.SocketStatus
import dev.yangsijun.rafia.data.enums.PlayerStatus
import dev.yangsijun.rafia.data.manager.EventManager
import dev.yangsijun.rafia.data.manager.JobManager
import dev.yangsijun.rafia.data.player.Player
import dev.yangsijun.rafia.domain.room.domain.Room
import dev.yangsijun.rafia.domain.room.service.RoomService
import dev.yangsijun.rafia.global.Util
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
        if (message.status != SocketStatus.READY)
            throw IllegalArgumentException("유효하지 않은 status")
        val room = roomService.findById(message.roomId) // TODO 예외처리 필요함
        val player: Player = room.players.first { it.user.id == message.userId } // TODO 예외 나옴
        room.players.remove(player)
        try {
            if (message.data.userStatus == PlayerStatus.NOT_READY) {
                room.players.add(
                    Player(
                        player.user,
                        player.id,
                        player.job,
                        PlayerStatus.NOT_READY,
                        player.interactions
                    )
                )
                roomService.save(room)
            } else if (message.data.userStatus == PlayerStatus.READY) {
                room.players.add(
                    Player(
                        player.user,
                        player.id,
                        player.job,
                        PlayerStatus.READY,
                        player.interactions
                    )
                )
                roomService.save(room)
            }
            sendingOperations.convertAndSend("/topic/" + message.roomId, message)
            if (isStartAble(room)) {
                // TODO 게임 시작하기
                eventManager.gameStart(message, room)
                jobManager.setJob(message, room)
            }
            return
        } catch (ex: NoSuchElementException) {
            throw IllegalArgumentException("방 안에 존재하지 않는 유저")
        }
        //TODO 에러 발생
    }

    fun isStartAble(room: Room): Boolean {
        val isAllReady: Boolean = room.players.count { it.readyStatus == PlayerStatus.READY } == room.players.size
        val canStartHeadCount: Boolean = room.players.count() >= Util.MIN_ROOM_START_PLAYER
        val isWait: Boolean = room.eventStatus == EventStatus.WAIT
        return (isAllReady and canStartHeadCount and isWait)
    }
}