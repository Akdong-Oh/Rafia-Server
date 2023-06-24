package dev.yangsijun.rafia.socket.message.base

import dev.yangsijun.rafia.data.enums.GameStatus
import java.util.*

abstract class AdminBaseMessage(
    status: GameStatus,
    roomId: UUID,
): BaseMessage(status, roomId) {
    abstract override val data: Any
}
