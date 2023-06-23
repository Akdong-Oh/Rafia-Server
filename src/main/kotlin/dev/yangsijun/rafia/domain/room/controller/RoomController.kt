package dev.yangsijun.rafia.domain.room.controller

import dev.yangsijun.rafia.domain.room.domain.Room
import dev.yangsijun.rafia.domain.room.dto.CreateRoom
import dev.yangsijun.rafia.domain.room.service.RoomService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/api/room")
class RoomController(
    private val roomService: RoomService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping("/rooms")
    fun getAllRooms(): ResponseEntity<List<Room>> {
        return ResponseEntity.ok()
            .body(roomService.findAll())
        log.trace("RoomController#getAllRooms : ${LocalDateTime.now()}")
    }

    @GetMapping("/{id}")
    fun getRoomById(@PathVariable id: UUID): ResponseEntity<Room> {
        return ResponseEntity.ok()
            .body(roomService.findById(id))
        log.trace("RoomController#getRoomById : ${LocalDateTime.now()}")
    }

    @PostMapping
    fun createRoom(@RequestBody roomDto: CreateRoom): ResponseEntity<Room> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(roomService.create(roomDto))
        log.trace("RoomController#createRoom : ${LocalDateTime.now()}")
    }

    @DeleteMapping("/{id}")
    fun deleteRoom(@PathVariable id: UUID): ResponseEntity<Room> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(roomService.deleteById(id))
        log.trace("RoomController#deleteRoom : ${LocalDateTime.now()}")
    }
}
