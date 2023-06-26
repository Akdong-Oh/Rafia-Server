package dev.yangsijun.rafia.socket.message.data

import dev.yangsijun.rafia.data.enums.SkillStatus
import java.util.UUID

data class Interaction(
    val skill: SkillStatus,
    val senderId: UUID,
    val receiverId: UUID
)
