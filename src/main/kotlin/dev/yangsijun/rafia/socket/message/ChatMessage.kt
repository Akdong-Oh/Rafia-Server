package dev.yangsijun.rafia.socket.message

import dev.yangsijun.rafia.data.enums.GameStatus
import dev.yangsijun.rafia.socket.message.base.UserBaseMessage
import dev.yangsijun.rafia.socket.message.data.Chat
import dev.yangsijun.rafia.socket.message.data.Entry
import java.util.UUID

data class ChatMessage(
    override val status: GameStatus,
    override val roomId: UUID,
    override val userId: UUID,
    override val data: Chat
): UserBaseMessage(status, roomId, userId)