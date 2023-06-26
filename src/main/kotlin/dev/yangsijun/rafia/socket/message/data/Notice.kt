package dev.yangsijun.rafia.socket.message.data

import dev.yangsijun.rafia.data.enums.NoticeStatus
import java.util.UUID

data class Notice(
    val noticeStatus: NoticeStatus,
    val userId: UUID?,
    val job: String?
)
