package dev.yangsijun.rafia.data.job

import dev.yangsijun.rafia.data.enums.SkillStatus
import dev.yangsijun.rafia.data.group.Criminal

class Mafia : Criminal, BaseJob() {
    override val skillStatuses: List<SkillStatus> = listOf(SkillStatus.VOTE, SkillStatus.KILL)

    override fun getName(): String {
        return Mafia.NAME
    }

    companion object {
        const val NAME: String = "Mafia"
    }
}