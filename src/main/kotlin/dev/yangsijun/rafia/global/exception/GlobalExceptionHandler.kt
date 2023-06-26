package dev.yangsijun.rafia.global.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import kotlin.IllegalArgumentException


@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException::class)
    protected fun illegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        return ResponseEntity<ErrorResponse>(ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.message.toString()).build(), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalStateException::class)
    protected fun illegalStateException(ex: IllegalStateException): ResponseEntity<ErrorResponse> {
        return ResponseEntity<ErrorResponse>(ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.message.toString()).build(), HttpStatus.BAD_REQUEST)
    }
}