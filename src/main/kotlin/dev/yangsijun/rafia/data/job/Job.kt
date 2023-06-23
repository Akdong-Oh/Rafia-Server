package dev.yangsijun.rafia.data.job

import dev.yangsijun.rafia.data.enums.Skill

abstract class Job {

    abstract val skills: List<Skill>

    fun killAble(): Boolean {
        return skills.any { it == Skill.KILL }
    }

    fun healAble(): Boolean {
        return skills.any { it == Skill.HEAL }
    }

    fun votAble(): Boolean {
        return skills.any { it == Skill.VOTE }
    }

    fun seeAble(): Boolean {
        return skills.any { it == Skill.SEEK }
    }

    open fun interact(interactions: List<Skill>): List<Skill> {
        val list: MutableList<Skill> = interactions.toMutableList()
        val isKilled = interactions.any { it == Skill.KILL }
        val isHealed = interactions.any { it == Skill.HEAL }
        if (isHealed and isKilled) {
            list.removeAll(listOf(Skill.KILL, Skill.HEAL))
        }
        return list.toList()
    }

    fun countVoted(interactions: List<Skill>): Int {
        return interactions.count { it == Skill.VOTE }
    }

    fun isSeeked(interactions: List<Skill>): Int {
        return interactions.count { it == Skill.SEEK }
    }

    // TODO 이 로직은 Manager 가야할 듯?
    //  대충 구상하자면 Manager가 요청 받고, Player 상태 변경(리스트에 add, 미리 있는 값에 상쇄 가능)
    //  Manager가 마지막으로 유저 정보 확인하고 유저별로 결과 notice (딱히 별일 없으면 리턴 X)
    //  아니 근데 킬하고 하는건 유저가 하는게 맞음
    //  그런 상태를 읽고 결과를 내는게 Manager고
}