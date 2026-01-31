package com.mauricio.apimonitoring.dto

import java.time.LocalDateTime
import java.util.UUID

class UserResponse(
    val id: UUID,
    val alias: String,
    val email: String,
    val password: String,
    val createdAt: LocalDateTime
)
