package dev.yangsijun.rafia.data.job

import dev.yangsijun.rafia.data.enums.Skill
import dev.yangsijun.rafia.data.group.Citizen

class Police : Citizen, Job() {
    override val skills: List<Skill> = listOf(Skill.VOTE, Skill.SEEK)

    companion object {
        const val NAME: String = "Police"
    }
}