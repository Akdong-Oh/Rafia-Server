package dev.yangsijun.rafia.socket.message

import dev.yangsijun.rafia.data.enums.GameStatus
import dev.yangsijun.rafia.socket.message.base.AdminBaseMessage
import dev.yangsijun.rafia.socket.message.data.Entry
import dev.yangsijun.rafia.socket.message.data.Exit
import java.util.UUID

data class ExitMessage(
    override val status: GameStatus,
    override val roomId: UUID,
    override val data: Exit
): AdminBaseMessage(status, roomId)