package dev.yangsijun.rafia.socket.message.base

import dev.yangsijun.rafia.data.enums.SocketStatus
import java.util.UUID

abstract class UserBaseMessage(
    status: SocketStatus,
    roomId: UUID,
    open val userId: UUID
): BaseMessage(status, roomId) {
    abstract override val data: Any
}
