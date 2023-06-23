package dev.yangsijun.rafia.domain.user.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
class User (
    @Id
    val id: UUID,
    val name: String
)