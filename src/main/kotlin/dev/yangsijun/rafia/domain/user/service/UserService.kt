package dev.yangsijun.rafia.domain.user.service

import dev.yangsijun.rafia.domain.user.domain.User
import dev.yangsijun.rafia.domain.user.dto.CreateUser
import dev.yangsijun.rafia.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserService(private val userRepository: UserRepository) {
    fun create(userDto: CreateUser): User {
        if (userRepository.existsByName(userDto.name))
            throw IllegalArgumentException("Existed User, name: " + userDto.name)
        val user = User(UUID.randomUUID(), userDto.name)
        return userRepository.save(user)

    }

    fun findById(id: UUID): User {
        return userRepository.findById(id)
            .orElseThrow {
                IllegalArgumentException(
                    "Not Found User, id: $id"
                )
            }
    }

    fun update(reqUser: User): User {
        val existingUser: User = userRepository.findById(reqUser.id)
            .orElseThrow {
                IllegalArgumentException(
                    "Not Found User, id: " + reqUser.id
                )
            }
        if (userRepository.existsByName(name = reqUser.name))
            throw IllegalArgumentException("Existed User, name: " + reqUser.name)
        val updatedUserWithId = User(existingUser.id, reqUser.name)
        return userRepository.save(updatedUserWithId)
    }
}
