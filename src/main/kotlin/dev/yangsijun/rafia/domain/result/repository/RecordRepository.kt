package dev.yangsijun.rafia.domain.result.repository

import dev.yangsijun.rafia.domain.result.domain.Record
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RecordRepository : MongoRepository<Record, UUID> {
}