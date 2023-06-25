package dev.yangsijun.rafia.socket.listener

import dev.yangsijun.rafia.data.enums.GameStatus
import dev.yangsijun.rafia.data.player.Player
import dev.yangsijun.rafia.domain.room.domain.Room
import dev.yangsijun.rafia.domain.room.service.RoomService
import dev.yangsijun.rafia.socket.message.ExitMessage
import dev.yangsijun.rafia.socket.message.data.Exit
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class WebSocketEventListener(
    private val sendingOperations: SimpMessageSendingOperations,
    private val roomService: RoomService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
//        try {
//            task(event)
//        } catch (ex: NoSuchElementException) {
//            // 그냥 못찾은거라 넘겨도 됨, 로그만 하고
//        } finally {
//            log.info("[Disconnected] websocket session id : {}", event.sessionId)
//        }
        // 요청 시마다 세션 id가 계속 바꿔서 의미가 없을듯
        // 걍 잠수 타면 어쩔 수 없는걸로
        // 아니면 우리쪽에서 Ping Pong 구현하면 되긴 함
        // TODO 굳이 ping pong이 아니라 일방적으로 client에서 요청을 보내도 ㄱㅊ을거 같고
    }

    private fun task(event: SessionDisconnectEvent) {
        val headerAccessor: StompHeaderAccessor = StompHeaderAccessor.wrap(event.message)
        val sessionId: String? = headerAccessor.sessionId
        val rooms: List<Room> = roomService.findAll()
        rooms.forEach { room -> // TODO 반복문 돌 때마다 없으면 에러가 발생해서 코드 나중에 수정해야 함
            val player: Player = room.players.first { player -> player.id == sessionId } // TODO 없으면 예외 발생, 예외처리하기
            if (room.players.count() <= 1)
                roomService.deleteById(room.id)
            else {
                room.players.remove(player)
                roomService.save(room)
                val message: ExitMessage = ExitMessage(
                    GameStatus.EXIT,
                    room.id,
                    player.user.id,
                    Exit(player.user.name)
                )
                sendingOperations.convertAndSend("/topic/" + message.roomId, message)
            }
        }
    }
}