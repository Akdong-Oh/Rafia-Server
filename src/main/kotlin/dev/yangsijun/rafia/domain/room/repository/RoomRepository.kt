package dev.yangsijun.rafia.domain.room.repository

import dev.yangsijun.rafia.domain.room.domain.Room
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RoomRepository : CrudRepository<Room, UUID> {
//    fun findByName(name: String): Room?
}