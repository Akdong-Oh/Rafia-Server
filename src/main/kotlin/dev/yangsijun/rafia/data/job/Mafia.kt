package dev.yangsijun.rafia.data.job

import dev.yangsijun.rafia.data.enums.Skill
import dev.yangsijun.rafia.data.group.Criminal

class Mafia : Criminal, Job() {
    override val skills: List<Skill> = listOf(Skill.VOTE, Skill.KILL)

    companion object {
        const val NAME: String = "Mafia"
    }
}