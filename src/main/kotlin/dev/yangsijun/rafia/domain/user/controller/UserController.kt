package dev.yangsijun.rafia.domain.user.controller

import dev.yangsijun.rafia.domain.user.domain.User
import dev.yangsijun.rafia.domain.user.dto.CreateUser
import dev.yangsijun.rafia.domain.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService) {

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: UUID): ResponseEntity<User> {
        return ResponseEntity.ok(userService.findById(id))
    }

    @PostMapping
    fun createUser(@RequestBody userDto: CreateUser): ResponseEntity<User> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.create(userDto))
    }

    @PutMapping
    fun updateUser(@RequestBody user: User): ResponseEntity<User> {
        return ResponseEntity.ok(userService.update(user))
    }
}
