package dev.yangsijun.rafia.socket

import dev.yangsijun.rafia.data.enums.PlayerStatus
import dev.yangsijun.rafia.data.player.Player
import dev.yangsijun.rafia.domain.room.domain.Room
import dev.yangsijun.rafia.domain.room.repository.RoomRepository
import dev.yangsijun.rafia.domain.room.service.RoomService
import dev.yangsijun.rafia.domain.user.domain.User
import dev.yangsijun.rafia.domain.user.repository.UserRepository
import dev.yangsijun.rafia.domain.user.service.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.*


@Component
class DummyRoomCommandLineRunner(
    val roomRepository: RoomRepository,
    val userRepository: UserRepository
) : CommandLineRunner {
    override fun run(vararg args: String) {
        userRepository.deleteAll()
        roomRepository.deleteAll()
        val readiedPlayers: MutableList<Player> = mutableListOf(
            Player(User(UUID.fromString("b9dc156f-b865-44ce-ab73-2ef65f706988"), "DummyUser1"), "sessionId", null, PlayerStatus.READY, mutableMapOf()),
            Player(User(UUID.randomUUID(), "DummyUser2"), "sessionId", null, PlayerStatus.READY, mutableMapOf()),
            Player(User(UUID.randomUUID(), "DummyUser3"), "sessionId", null, PlayerStatus.READY, mutableMapOf()),
            Player(User(UUID.randomUUID(), "DummyUser4"), "sessionId", null, PlayerStatus.READY, mutableMapOf()),
            Player(User(UUID.randomUUID(), "DummyUser5"), "sessionId", null, PlayerStatus.READY, mutableMapOf()),
            Player(User(UUID.randomUUID(), "DummyUser6"), "sessionId", null, PlayerStatus.READY, mutableMapOf()),
            Player(User(UUID.randomUUID(), "DummyUser7"), "sessionId", null, PlayerStatus.READY, mutableMapOf()),
        )
        val users = readiedPlayers.map { it.user }
        val readiedRoom = Room(
            id = UUID.fromString("61feae44-7b40-4d60-98e1-8bd1a6a9613e"),
            name= "RoomDummy1",
            players = readiedPlayers
        )
        userRepository.saveAll(users)
        roomRepository.save(readiedRoom)
    }
}
