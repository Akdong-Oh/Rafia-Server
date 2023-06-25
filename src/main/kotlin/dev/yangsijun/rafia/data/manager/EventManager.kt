package dev.yangsijun.rafia.data.manager

import dev.yangsijun.rafia.data.enums.EventStatus
import dev.yangsijun.rafia.data.enums.SocketStatus
import dev.yangsijun.rafia.domain.room.domain.Room

import dev.yangsijun.rafia.domain.room.service.RoomService
import dev.yangsijun.rafia.socket.message.EventMessage
import dev.yangsijun.rafia.socket.message.ReadyMessage
import dev.yangsijun.rafia.socket.message.data.Event
import org.slf4j.LoggerFactory
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
open class EventManager(
    private val sendingOperations: SimpMessageSendingOperations,
    private val roomService: RoomService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun gameStart(message: ReadyMessage, room: Room) {
        val eventMessage = EventMessage(SocketStatus.EVENT, message.roomId, Event(EventStatus.NIGHT))
        room.eventStatus = eventMessage.data.status
        roomService.saveAsync(room)
        sendingOperations.convertAndSend("/topic/" + eventMessage.roomId, eventMessage)
        log.trace("EventManager#gameStart at : ${LocalDateTime.now()}")
        return
    }

    fun flowGame(message: ReadyMessage, room: Room) {

    }
}