package dev.yangsijun.rafia.socket.controller

import dev.yangsijun.rafia.data.enums.*
import dev.yangsijun.rafia.data.manager.EventManager
import dev.yangsijun.rafia.data.manager.JobManager
import dev.yangsijun.rafia.data.player.Player
import dev.yangsijun.rafia.domain.room.domain.Room
import dev.yangsijun.rafia.domain.room.service.RoomService
import dev.yangsijun.rafia.global.GameUtil
import dev.yangsijun.rafia.global.RoomUtil
import dev.yangsijun.rafia.socket.message.InteractionMessage
import dev.yangsijun.rafia.socket.message.ReadyMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageHeaderAccessor

import org.springframework.messaging.simp.SimpMessageSendingOperations

import org.springframework.web.bind.annotation.RestController


@RestController
class InteractionController(
    private val sendingOperations: SimpMessageSendingOperations,
    private val roomService: RoomService
) {
    @MessageMapping("/interaction")
    fun enter(message: InteractionMessage, headerAccessor: SimpMessageHeaderAccessor) {
        if (message.status != SocketStatus.INTERACTION) {
            val exMessage = GameUtil.errorMessage(message, ErrorCode.C0000) // 유효하지 않은 status
            sendingOperations.convertAndSend("/topic/" + exMessage.roomId, exMessage)
        }
        val room = roomService.findById(message.roomId) // TODO 예외처리 필요함
        room.players.forEach { // 이미 있는 상호작용인 경우 기존 값 제거
            it.interactions.remove(message.data.senderId, message.data.skill)
        }
        room.players.forEach { // 돌면서 receiver의 ID인 경우 저장
            if (it.user.id == message.data.receiverId) { //TODO k만 같아도 대체되고 있음, 사실 k, v 다 같아야 대체 되어야 하는데, 어차피 지금 상황에선 상관없어서 냅둡. (한 사람이 특정 이벤트에 투표, 킬, 힐 중 1번이라도 여러변 할 수 없음)
                it.interactions[message.data.senderId] = message.data.skill
            }
        }
        roomService.save(room)
        sendingOperations.convertAndSend("/topic/" + message.roomId, message)
    }

}