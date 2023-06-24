package dev.yangsijun.rafia.socket.message.base

import dev.yangsijun.rafia.data.enums.GameStatus
import java.util.*

abstract class BaseMessage(
    open val status: GameStatus,
    open val roomId: UUID,
) {
    abstract val data: Any
}
