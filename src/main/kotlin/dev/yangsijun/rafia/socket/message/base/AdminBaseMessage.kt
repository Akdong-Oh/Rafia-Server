package dev.yangsijun.rafia.socket.message.base

import dev.yangsijun.rafia.data.enums.SocketStatus
import java.util.*

abstract class AdminBaseMessage(
    status: SocketStatus,
    roomId: UUID,
): BaseMessage(status, roomId) {
    abstract override val data: Any
}
