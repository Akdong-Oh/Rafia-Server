package dev.yangsijun.rafia.socket.message.base

import dev.yangsijun.rafia.data.enums.SocketStatus
import java.util.*

abstract class BaseMessage(
    open val status: SocketStatus,
    open val roomId: UUID,
) {
    abstract val data: Any
}
