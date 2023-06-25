package dev.yangsijun.rafia.socket.message

import dev.yangsijun.rafia.data.enums.SocketStatus
import dev.yangsijun.rafia.socket.message.base.UserBaseMessage
import dev.yangsijun.rafia.socket.message.data.Ready
import java.util.UUID

data class ReadyMessage(
    override val status: SocketStatus,
    override val roomId: UUID,
    override val userId: UUID,
    override val data: Ready,
): UserBaseMessage(status, roomId, userId)