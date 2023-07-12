package dev.yangsijun.rafia.data.job

import dev.yangsijun.rafia.data.enums.SkillStatus
import dev.yangsijun.rafia.data.group.Citizen

class Resident : Citizen, BaseJob() {
    override var skillStatuses: List<SkillStatus> = listOf(SkillStatus.VOTE)

    override fun getName(): String {
        return Resident.NAME
    }

    companion object {
        const val NAME: String = "Resident"
    }
}