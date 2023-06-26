package dev.yangsijun.rafia.domain.result.service

import dev.yangsijun.rafia.domain.result.domain.Record
import dev.yangsijun.rafia.domain.result.repository.RecordRepository
import dev.yangsijun.rafia.domain.room.domain.Room
import org.springframework.stereotype.Service

@Service
class RecordService(
    private val repository: RecordRepository
) {

    fun save(room: Room, isCriminalWin: Boolean) {
        repository.save(Record(room.id, room.name, isCriminalWin, room.players))
    }

}