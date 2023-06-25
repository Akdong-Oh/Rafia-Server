package dev.yangsijun.rafia.domain.room.domain

import dev.yangsijun.rafia.data.enums.EventStatus
import dev.yangsijun.rafia.data.player.Player

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.util.*

@RedisHash(value = "room")
class Room(
    @Id
    val id: UUID = UUID.randomUUID(),
    val name: String,
    var eventStatus: EventStatus = EventStatus.WAIT,
    val players: MutableList<Player> = mutableListOf()
) {
}