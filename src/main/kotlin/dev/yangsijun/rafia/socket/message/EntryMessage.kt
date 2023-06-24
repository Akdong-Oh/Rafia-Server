package dev.yangsijun.rafia.socket.message

import dev.yangsijun.rafia.data.enums.GameStatus
import dev.yangsijun.rafia.socket.message.base.AdminBaseMessage
import dev.yangsijun.rafia.socket.message.data.Entry
import java.util.UUID

data class EntryMessage(
    override val status: GameStatus,
    override val roomId: UUID,
    override val data: Entry
): AdminBaseMessage(status, roomId)