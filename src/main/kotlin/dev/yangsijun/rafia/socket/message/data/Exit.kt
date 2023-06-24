package dev.yangsijun.rafia.socket.message.data

import java.util.UUID

data class Exit(
    val userId: UUID,
    val userName: String
)
