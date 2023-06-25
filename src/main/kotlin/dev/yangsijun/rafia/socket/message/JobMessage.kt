package dev.yangsijun.rafia.socket.message

import dev.yangsijun.rafia.data.enums.EventStatus
import dev.yangsijun.rafia.data.enums.SocketStatus
import dev.yangsijun.rafia.socket.message.base.AdminBaseMessage
import dev.yangsijun.rafia.socket.message.base.UserBaseMessage
import dev.yangsijun.rafia.socket.message.data.Event
import dev.yangsijun.rafia.socket.message.data.Job
import java.util.UUID

data class JobMessage(
    override val status: SocketStatus,
    override val roomId: UUID,
    override val data: Job
): AdminBaseMessage(status, roomId)