package dev.yangsijun.rafia.socket.message

import dev.yangsijun.rafia.data.enums.SocketStatus
import dev.yangsijun.rafia.socket.message.base.UserBaseMessage
import dev.yangsijun.rafia.socket.message.data.Chat
import java.util.UUID

data class ChatMessage(
    override val status: SocketStatus,
    override val roomId: UUID,
    override val userId: UUID,
    override val data: Chat
): UserBaseMessage(status, roomId, userId)