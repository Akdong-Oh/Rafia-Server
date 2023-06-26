package dev.yangsijun.rafia.data.manager

import dev.yangsijun.rafia.data.enums.*
import dev.yangsijun.rafia.data.group.Citizen
import dev.yangsijun.rafia.data.group.Criminal
import dev.yangsijun.rafia.data.player.Player
import dev.yangsijun.rafia.domain.result.service.RecordService
import dev.yangsijun.rafia.domain.room.domain.Room

import dev.yangsijun.rafia.domain.room.service.RoomService
import dev.yangsijun.rafia.global.GameUtil
import dev.yangsijun.rafia.socket.message.EventMessage
import dev.yangsijun.rafia.socket.message.NoticeMessage
import dev.yangsijun.rafia.socket.message.ReadyMessage
import dev.yangsijun.rafia.socket.message.data.Event
import dev.yangsijun.rafia.socket.message.data.Notice
import org.slf4j.LoggerFactory
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class EventManager(
    private val sendingOperations: SimpMessageSendingOperations,
    private val roomService: RoomService,
    private val recordService: RecordService
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private var isEnd: Boolean = false

    fun gameStart(message: ReadyMessage, room: Room) {
        room.players.forEach {
            it.readyStatus = ReadyStatus.NOT_READY
        }
        roomService.saveAsync(room)
        flowGame(message, room)
        log.trace("EventManager#gameStart at : ${LocalDateTime.now()}")
        return
    }

    @Async
    fun flowGame(message: ReadyMessage, room: Room) {
        while (!isEnd) {
            handleEvent(room.id, EventStatus.NIGHT, GameUtil.NIGHT_TIME)
            nightResultNotice(room.id)
            handleEvent(room.id, EventStatus.DAY, GameUtil.DAY_TIME_PER_ALIVE_PLAYER * room.players.size)
            handleEvent(room.id, EventStatus.VOTE, GameUtil.VOTE_TIME)
            voteResultNotice(room.id)
            handleEvent(room.id, EventStatus.OBJECTION, GameUtil.OBJECTION_TIME)
            handleEvent(room.id, EventStatus.CONFIRMATION, GameUtil.CONFIRMATION_TIME)
        }
        val endMessage = EventMessage(SocketStatus.EVENT, room.id, Event(EventStatus.END, 0))
        sendingOperations.convertAndSend("/topic/" + endMessage.roomId, endMessage)
    }

    private fun handleEvent(roomId: UUID, eventStatus: EventStatus, sleepTime: Long) {
        val room:Room = roomService.findById(roomId)
        val eventMessage = EventMessage(SocketStatus.EVENT, room.id, Event(eventStatus, sleepTime))
        room.eventStatus = eventMessage.data.status
        roomService.saveAsync(room)
        sendingOperations.convertAndSend("/topic/" + eventMessage.roomId, eventMessage)
        log.trace("Start Event ${eventStatus.name} | Sec : $sleepTime")
        sleepTime.let { TimeUnit.SECONDS.sleep(it) }
        checkEnd(room.id)
    }

    private fun checkEnd(roomId: UUID) {
        val room:Room = roomService.findById(roomId)
        val aliveCriminalCount = room.players.count { (it.job is Criminal) and (it.playerStatus != PlayerStatus.DEAD) }
        val aliveCitizenCount = room.players.count { (it.job is Citizen) and (it.playerStatus != PlayerStatus.DEAD) }
        if (aliveCriminalCount >= aliveCitizenCount) {  // 마피아 win
            log.trace("Criminal Win")
            isEnd = true
            reset(room, true)
        }
        if (aliveCriminalCount <= 0) {  // 시민 win
            log.trace("Citizen Win")
            isEnd = true
            reset(room, false)
        }
        return
    }

    private fun voteResultNotice(roomId: UUID) {
        val room:Room = roomService.findById(roomId)
        val maxVoteCount = room.players.maxOfOrNull { it.interactions.count { interaction -> interaction.value == SkillStatus.VOTE } } ?: 0
        val maxVotePlayers = room.players.filter { it.interactions.count { interaction -> interaction.value == SkillStatus.VOTE } == maxVoteCount }
        if (maxVotePlayers.isEmpty()) {
            val noticeMessage = NoticeMessage(SocketStatus.NOTICE, room.id, Notice(NoticeStatus.PASS, null, null))
            sendingOperations.convertAndSend("/topic/" + noticeMessage.roomId, noticeMessage)
            log.trace("Pass Vote : ${NoticeStatus.PASS.name} ")
        } else if (maxVotePlayers.size == 1) {
            val maxVotePlayer = maxVotePlayers.first()
            val executedPlayer = Player(
                maxVotePlayer.user,
                maxVotePlayer.id,
                maxVotePlayer.job,
                maxVotePlayer.readyStatus,
                PlayerStatus.DEAD,
                mutableMapOf()
            )
            room.players.remove(maxVotePlayer)
            room.players.add(executedPlayer)
            roomService.saveAsync(room)
            val noticeMessage = NoticeMessage(SocketStatus.NOTICE, room.id, Notice(NoticeStatus.EXECUTED, maxVotePlayer.user.id, maxVotePlayer.job?.getName()))
            sendingOperations.convertAndSend("/topic/" + noticeMessage.roomId, noticeMessage)
            log.trace("User ${maxVotePlayer.user.name} : ${NoticeStatus.EXECUTED.name} ")
        } else {
            val noticeMessage = NoticeMessage(SocketStatus.NOTICE, room.id, Notice(NoticeStatus.SAME_COUNT_VOTE, null, null))
            sendingOperations.convertAndSend("/topic/" + noticeMessage.roomId, noticeMessage)
            log.trace("Pass Vote : ${NoticeStatus.SAME_COUNT_VOTE.name} ")
        }
        room.players.forEach { player: Player ->
            player.interactions.clear()
            roomService.saveAsync(room)
        }
        return
    }


    private fun nightResultNotice(roomId: UUID) {
        val room:Room = roomService.findById(roomId)

        // TODO 인터렉션 컨트롤러 만들기, 투표를 제외하고는 모든 인터렉션에 한 SKILL만 있어야 함
        //  투표의 경우, 한 유저가 한 사람에게 투표 가능

        room.players.forEach { player: Player ->
            val values = player.interactions.values
            if (values.any { v -> v == SkillStatus.SEEK }) {
                val noticeMessage = NoticeMessage(
                    SocketStatus.NOTICE,
                    room.id,
                    Notice(NoticeStatus.HEALED, player.user.id, player.job?.getName())
                )
                sendingOperations.convertAndSend("/topic/" + noticeMessage.roomId, noticeMessage)
                log.trace("User ${player.user.name} : ${NoticeStatus.SEEKED.name}, Job : ${player.job?.getName()} ")
            }
            if (values.any { v -> v == SkillStatus.KILL } and values.any { v -> v == SkillStatus.HEAL }) {
                val noticeMessage =
                    NoticeMessage(SocketStatus.NOTICE, room.id, Notice(NoticeStatus.HEALED, player.user.id, player.job?.getName()))
                sendingOperations.convertAndSend("/topic/" + noticeMessage.roomId, noticeMessage)
                log.trace("User ${player.user.name} : ${NoticeStatus.HEALED.name} ")
            } else if (values.any { v -> v == SkillStatus.KILL }) {
                val noticeMessage =
                    NoticeMessage(SocketStatus.NOTICE, room.id, Notice(NoticeStatus.KILLED, player.user.id, player.job?.getName()))
                sendingOperations.convertAndSend("/topic/" + noticeMessage.roomId, noticeMessage)
                val killedPlayer = Player(
                    player.user,
                    player.id,
                    player.job,
                    player.readyStatus,
                    PlayerStatus.DEAD,
                    mutableMapOf()
                )
                room.players.remove(killedPlayer)
                room.players.add(killedPlayer)
                roomService.saveAsync(room)
                log.trace("User ${killedPlayer.user.name} : ${NoticeStatus.KILLED.name} ")
            } else {
                val noticeMessage =
                    NoticeMessage(SocketStatus.NOTICE, room.id, Notice(NoticeStatus.PASS, player.user.id, player.job?.getName()))
                sendingOperations.convertAndSend("/topic/" + noticeMessage.roomId, noticeMessage)
                log.trace("Pass Night : ${NoticeStatus.PASS.name} ")
            }
        }
        room.players.forEach { player: Player ->
            player.interactions.clear()
            roomService.saveAsync(room)
        }
        return
    }

    private fun reset(room: Room, isCriminalWin: Boolean) {
        recordService.save(room, isCriminalWin)
        roomService.renew(room)
        val renewMessage = EventMessage(SocketStatus.EVENT, room.id, Event(EventStatus.WAIT, 0))
        sendingOperations.convertAndSend("/topic/" + renewMessage.roomId, renewMessage)
        return
    }

}