package dev.yangsijun.rafia.domain.room.domain

import dev.yangsijun.rafia.data.player.Player
import dev.yangsijun.rafia.domain.room.enums.RoomStatus
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.redis.core.RedisHash
import java.util.*

@RedisHash(value = "room")
class Room(
    @Id
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val status: RoomStatus = RoomStatus.OPEN,
    val players: MutableList<Player> = mutableListOf()
) {
}