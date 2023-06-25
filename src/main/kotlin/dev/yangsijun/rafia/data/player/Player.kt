package dev.yangsijun.rafia.data.player

import dev.yangsijun.rafia.data.enums.GameStatus
import dev.yangsijun.rafia.data.enums.PlayerStatus
import dev.yangsijun.rafia.data.enums.Skill
import dev.yangsijun.rafia.data.job.Job
import dev.yangsijun.rafia.domain.user.domain.User

class Player(
    val user: User, // 기본적으론 UserId를 기준으로 조회/수정
    val id: String, // sessionId - 연결 끊겼을 때, 빼고는 사용 X
    val job: Job? = null, // lazy
    val status: PlayerStatus = PlayerStatus.NOT_READY,
    val interactions: List<Skill> = listOf()
)