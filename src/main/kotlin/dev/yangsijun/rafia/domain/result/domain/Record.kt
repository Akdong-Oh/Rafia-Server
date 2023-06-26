package dev.yangsijun.rafia.domain.result.domain

import dev.yangsijun.rafia.data.player.Player
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
class Record (
    @Id
    val id: UUID,
    val name: String,
    val isCriminalWin: Boolean,
    val players : List<Player>
)