package dev.yangsijun.rafia.data.job

import dev.yangsijun.rafia.data.enums.SkillStatus
import dev.yangsijun.rafia.data.group.Citizen

class Police : Citizen, BaseJob() {
    override val skillStatuses: List<SkillStatus> = listOf(SkillStatus.VOTE, SkillStatus.SEEK)

    override fun getName(): String {
        return Police.NAME
    }

    companion object {
        const val NAME: String = "Police"
    }
}