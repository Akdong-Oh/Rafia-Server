package dev.yangsijun.rafia.domain.user.repository

import dev.yangsijun.rafia.domain.user.domain.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

@Repository
interface UserRepository : MongoRepository<User, UUID> {
    fun existsByName(name: String): Boolean
}