package dev.yangsijun.rafia.socket.message

import dev.yangsijun.rafia.data.enums.EventStatus
import dev.yangsijun.rafia.data.enums.SocketStatus
import dev.yangsijun.rafia.socket.message.base.AdminBaseMessage
import dev.yangsijun.rafia.socket.message.base.UserBaseMessage
import dev.yangsijun.rafia.socket.message.data.Event
import java.util.UUID

data class EventMessage(
    override val status: SocketStatus,
    override val roomId: UUID,
    override val data: Event
): AdminBaseMessage(status, roomId)