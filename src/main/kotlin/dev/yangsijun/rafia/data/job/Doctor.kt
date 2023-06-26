package dev.yangsijun.rafia.data.job

import dev.yangsijun.rafia.data.enums.SkillStatus
import dev.yangsijun.rafia.data.group.Citizen

class Doctor : Citizen, BaseJob() {
    override var skillStatuses: List<SkillStatus> = listOf(SkillStatus.VOTE, SkillStatus.HEAL)
    override fun getName(): String {
        return Doctor.NAME
    }

    companion object {
        const val NAME: String = "Doctor"
    }
}