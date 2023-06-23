package dev.yangsijun.rafia

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories

@SpringBootApplication
@EnableRedisRepositories
class RafiaApplication

fun main(args: Array<String>) {
    runApplication<RafiaApplication>(*args)
}
