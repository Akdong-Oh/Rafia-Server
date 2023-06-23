package dev.yangsijun.rafia.data.player

import dev.yangsijun.rafia.data.enums.Skill
import dev.yangsijun.rafia.domain.user.domain.User

class Player(
    val user: User,
    val id: Int,
    val job: String? = null, // lazy
    val interactions: List<Skill> = listOf()
) {

}