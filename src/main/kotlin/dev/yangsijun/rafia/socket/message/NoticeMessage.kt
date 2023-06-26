package dev.yangsijun.rafia.socket.message

import dev.yangsijun.rafia.data.enums.SocketStatus
import dev.yangsijun.rafia.socket.message.base.AdminBaseMessage
import dev.yangsijun.rafia.socket.message.base.UserBaseMessage
import dev.yangsijun.rafia.socket.message.data.Chat
import dev.yangsijun.rafia.socket.message.data.Notice
import java.util.UUID

data class NoticeMessage(
    override val status: SocketStatus,
    override val roomId: UUID,
    override val data: Notice
): AdminBaseMessage(status, roomId)