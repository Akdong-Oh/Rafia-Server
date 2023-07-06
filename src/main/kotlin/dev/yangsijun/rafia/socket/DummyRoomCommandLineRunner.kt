package dev.yangsijun.rafia.socket

import dev.yangsijun.rafia.data.enums.PlayerStatus
import dev.yangsijun.rafia.data.enums.ReadyStatus
import dev.yangsijun.rafia.data.player.Player
import dev.yangsijun.rafia.domain.room.domain.Room
import dev.yangsijun.rafia.domain.room.repository.RoomRepository
import dev.yangsijun.rafia.domain.user.domain.User
import dev.yangsijun.rafia.domain.user.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.*


@Component
class DummyRoomCommandLineRunner(
    val roomRepository: RoomRepository,
    val userRepository: UserRepository
) : CommandLineRunner {
    override fun run(vararg args: String) {
        /*
        userRepository.deleteAll()
        roomRepository.deleteAll()
        val readiedPlayers: MutableList<Player> = mutableListOf(
            Player(User(UUID.fromString("b9dc156f-b865-44ce-ab73-2ef65f706988"), "DummyUser1"), "sessionId1", null, ReadyStatus.READY, PlayerStatus.ALIVE, mutableMapOf()),
            Player(User(UUID.randomUUID(), "DummyUser2"), "sessionId2", null, ReadyStatus.READY, PlayerStatus.ALIVE, mutableMapOf()),
            Player(User(UUID.randomUUID(), "DummyUser3"), "sessionId3", null, ReadyStatus.READY, PlayerStatus.ALIVE, mutableMapOf()),
            Player(User(UUID.randomUUID(), "DummyUser4"), "sessionId4", null, ReadyStatus.READY, PlayerStatus.ALIVE, mutableMapOf()),
            Player(User(UUID.randomUUID(), "DummyUser5"), "sessionId5", null, ReadyStatus.READY, PlayerStatus.ALIVE, mutableMapOf()),
            Player(User(UUID.randomUUID(), "DummyUser6"), "sessionId6", null, ReadyStatus.READY, PlayerStatus.ALIVE, mutableMapOf()),
            Player(User(UUID.randomUUID(), "DummyUser7"), "sessionId7", null, ReadyStatus.READY, PlayerStatus.ALIVE, mutableMapOf()),
        )
        val users = readiedPlayers.map { it.user }
        val readiedRoom = Room(
            id = UUID.fromString("61feae44-7b40-4d60-98e1-8bd1a6a9613e"),
            name= "RoomDummy1",
            players = readiedPlayers
        )
        userRepository.saveAll(users)
        roomRepository.save(readiedRoom)
         */
    }
}
