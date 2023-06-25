package dev.yangsijun.rafia.data.player

import dev.yangsijun.rafia.data.enums.PlayerStatus
import dev.yangsijun.rafia.data.enums.SkillStatus
import dev.yangsijun.rafia.data.job.BaseJob
import dev.yangsijun.rafia.domain.user.domain.User

class Player(
    val user: User, // 기본적으론 UserId를 기준으로 조회/수정
    val id: String, // sessionId - 연결 끊겼을 때, 빼고는 사용 X
    var job: BaseJob? = null, // lazy
    val readyStatus: PlayerStatus = PlayerStatus.NOT_READY,
    val interactions: List<SkillStatus> = listOf()
)