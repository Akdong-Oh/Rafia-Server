package dev.yangsijun.rafia.data.enums

enum class Event {
    WAIT,
    DAY,
    VOTE,
    OBJECTION, // 거절, 최후반론
    CONFIRMATION, // 확인, 찬반투표
    NIGHT,
    END
}