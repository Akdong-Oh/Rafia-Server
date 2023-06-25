package dev.yangsijun.rafia.socket.message.data

import dev.yangsijun.rafia.data.enums.PlayerStatus
import java.util.UUID

data class Ready(
    val userStatus: PlayerStatus
)
