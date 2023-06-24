package dev.yangsijun.rafia.socket.message.base

import dev.yangsijun.rafia.data.enums.GameStatus
import java.util.UUID

abstract class UserBaseMessage(
    status: GameStatus,
    roomId: UUID,
    open val userId: UUID
): BaseMessage(status, roomId) {
    abstract override val data: Any
}
