package dev.yangsijun.rafia.socket.message

import dev.yangsijun.rafia.data.enums.SocketStatus
import dev.yangsijun.rafia.socket.message.base.UserBaseMessage
import dev.yangsijun.rafia.socket.message.data.Entry
import java.util.UUID

data class EntryMessage(
    override val status: SocketStatus,
    override val roomId: UUID,
    override val userId: UUID,
    override val data: Entry
): UserBaseMessage(status, roomId, userId)