package dev.yangsijun.rafia.data.job

import dev.yangsijun.rafia.data.enums.SkillStatus

abstract class BaseJob {

    abstract var skillStatuses: List<SkillStatus>

    fun killAble(): Boolean {
        return skillStatuses.any { it == SkillStatus.KILL }
    }

    fun healAble(): Boolean {
        return skillStatuses.any { it == SkillStatus.HEAL }
    }

    fun votAble(): Boolean {
        return skillStatuses.any { it == SkillStatus.VOTE }
    }

    fun seeAble(): Boolean {
        return skillStatuses.any { it == SkillStatus.SEEK }
    }

    abstract fun getName(): String

    open fun interact(interactions: List<SkillStatus>): List<SkillStatus> {
        val list: MutableList<SkillStatus> = interactions.toMutableList()
        val isKilled = interactions.any { it == SkillStatus.KILL }
        val isHealed = interactions.any { it == SkillStatus.HEAL }
        if (isHealed and isKilled) {
            list.removeAll(listOf(SkillStatus.KILL, SkillStatus.HEAL))
        }
        return list.toList()
    }

    fun countVoted(interactions: List<SkillStatus>): Int {
        return interactions.count { it == SkillStatus.VOTE }
    }

    fun isSeeked(interactions: List<SkillStatus>): Int {
        return interactions.count { it == SkillStatus.SEEK }
    }

    // TODO 이 로직은 Manager 가야할 듯?
    //  대충 구상하자면 Manager가 요청 받고, Player 상태 변경(리스트에 add, 미리 있는 값에 상쇄 가능)
    //  Manager가 마지막으로 유저 정보 확인하고 유저별로 결과 notice (딱히 별일 없으면 리턴 X)
    //  아니 근데 킬하고 하는건 유저가 하는게 맞음
    //  그런 상태를 읽고 결과를 내는게 Manager고
}