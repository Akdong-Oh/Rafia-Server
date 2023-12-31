package dev.yangsijun.rafia.domain.room.service

import dev.yangsijun.rafia.data.enums.EventStatus
import dev.yangsijun.rafia.data.enums.PlayerStatus
import dev.yangsijun.rafia.data.enums.ReadyStatus
import dev.yangsijun.rafia.domain.room.domain.Room
import dev.yangsijun.rafia.domain.room.dto.CreateRoom

import dev.yangsijun.rafia.domain.room.repository.RoomRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class RoomService(
    private val repository: RoomRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun findAll(): List<Room> {
        return repository.findAll().toList()
        log.trace("RoomService#findAll : ${LocalDateTime.now()}")
    }

    fun create(roomDto: CreateRoom): Room {
        if (findAll().any { it.name == roomDto.name }) {
            throw IllegalArgumentException("Existed Room, name: " + roomDto.name)
        }
        return repository.save(Room(name = roomDto.name, eventStatus = EventStatus.WAIT))
        log.trace("RoomService#create [$roomDto.name] : ${LocalDateTime.now()}")
    }

    fun findById(id: UUID): Room {
        val room = repository.findByIdOrNull(id) ?: throw IllegalArgumentException("Not found Room, id : $id")
        return room
        log.trace("RoomService#findById [$id] : ${LocalDateTime.now()}")
    }

    fun deleteById(id: UUID): Room {
        val room = findById(id)
        repository.delete(room)
        return room
        log.trace("RoomService#deleteById [$id] : ${LocalDateTime.now()}")
    }

    fun existById(id: UUID): Boolean {
        return repository.existsById(id)
        log.trace("RoomService#existById [$id] : ${LocalDateTime.now()}")
    }

    fun save(room: Room): Room {
        return repository.save(room)
        log.trace("RoomService#existById [$room] : ${LocalDateTime.now()}")
    }

    @Async
    fun saveAsync(room: Room) {
        this.save(room)
        //log.trace("RoomService#saveAsync at : ${LocalDateTime.now()}")
    }

    fun renew(room: Room): Room {
        Room(room.id, room.name, EventStatus.WAIT, room.players)
        room.players.forEach {
            it.job = null
            it.interactions = mutableMapOf()
            it.readyStatus = ReadyStatus.NOT_READY
            it.playerStatus = PlayerStatus.ALIVE
        }
        return repository.save(room)
        log.trace("RoomService#existById [$room] : ${LocalDateTime.now()}")
    }
}