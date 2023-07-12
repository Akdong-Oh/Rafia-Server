package dev.yangsijun.rafia.data.manager

import dev.yangsijun.rafia.data.enums.NoticeStatus
import dev.yangsijun.rafia.data.enums.SocketStatus
import dev.yangsijun.rafia.data.job.*
import dev.yangsijun.rafia.domain.room.domain.Room
import dev.yangsijun.rafia.domain.room.service.RoomService
import dev.yangsijun.rafia.socket.message.JobMessage
import dev.yangsijun.rafia.socket.message.ReadyMessage
import dev.yangsijun.rafia.socket.message.data.Job
import org.slf4j.LoggerFactory
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Component

@Component
class JobManager(
    private val sendingOperations: SimpMessageSendingOperations,
    private val roomService: RoomService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun setJob(message: ReadyMessage, room: Room) {
        val jobQueue: ArrayDeque<BaseJob> = createJobQueue(room.players.size)
        room.players.forEach {
            val job = jobQueue.removeFirst()
            it.job = job
            val jobMessage = JobMessage(SocketStatus.SET_JOB, message.roomId, Job(it.user.id, job.getName()))
            sendingOperations.convertAndSend("/topic/" + jobMessage.roomId, jobMessage)
            log.trace("User ${it.user.name} Set Job : ${job} ")
        }
        roomService.saveAsync(room)
    }


    private fun createJobQueue(size: Int): ArrayDeque<BaseJob> {
        val jobs: MutableList<BaseJob> = mutableListOf(Mafia(), Mafia(), Police(), Doctor())
        jobs.addAll(MutableList(size - jobs.size) { Resident() })
        return ArrayDeque(jobs.shuffled())
    }
}