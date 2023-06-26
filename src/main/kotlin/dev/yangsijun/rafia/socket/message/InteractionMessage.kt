package dev.yangsijun.rafia.socket.message

import dev.yangsijun.rafia.data.enums.SocketStatus
import dev.yangsijun.rafia.socket.message.base.UserBaseMessage
import dev.yangsijun.rafia.socket.message.data.Chat
import dev.yangsijun.rafia.socket.message.data.Interaction
import java.util.UUID

data class InteractionMessage(
    override val status: SocketStatus,
    override val roomId: UUID,
    override val userId: UUID,
    override val data: Interaction
): UserBaseMessage(status, roomId, userId)