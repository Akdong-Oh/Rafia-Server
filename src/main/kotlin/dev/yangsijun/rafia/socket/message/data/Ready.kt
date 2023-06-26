package dev.yangsijun.rafia.socket.message.data

import dev.yangsijun.rafia.data.enums.ReadyStatus

data class Ready(
    val userStatus: ReadyStatus
)
