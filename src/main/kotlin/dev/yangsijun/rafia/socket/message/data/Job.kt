package dev.yangsijun.rafia.socket.message.data

import java.util.UUID

data class Job(
    val userId: UUID,
    val job: String
)
