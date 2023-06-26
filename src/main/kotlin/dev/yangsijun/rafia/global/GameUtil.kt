package dev.yangsijun.rafia.global

import dev.yangsijun.rafia.data.enums.ErrorCode
import dev.yangsijun.rafia.data.enums.SocketStatus
import dev.yangsijun.rafia.socket.message.ErrorMessage
import dev.yangsijun.rafia.socket.message.base.BaseMessage
import dev.yangsijun.rafia.socket.message.data.ExData

class GameUtil {
    companion object {
        const val NIGHT_TIME: Long = 25
        const val DAY_TIME_PER_ALIVE_PLAYER: Long = 15
        const val VOTE_TIME: Long = 15
        const val OBJECTION_TIME: Long = 15
        const val CONFIRMATION_TIME: Long = 10

        fun errorMessage(message: BaseMessage, errorCode: ErrorCode): ErrorMessage {
            return ErrorMessage(SocketStatus.ERROR, message.roomId, ExData(errorCode.msg, errorCode.code))
        }
    }
}
