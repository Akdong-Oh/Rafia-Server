package dev.yangsijun.rafia.socket.message

import dev.yangsijun.rafia.data.enums.SocketStatus
import dev.yangsijun.rafia.socket.message.base.AdminBaseMessage
import dev.yangsijun.rafia.socket.message.data.ExData
import java.util.UUID

data class ErrorMessage(
    override val status: SocketStatus,
    override val roomId: UUID,
    override val data: ExData
): AdminBaseMessage(status, roomId)