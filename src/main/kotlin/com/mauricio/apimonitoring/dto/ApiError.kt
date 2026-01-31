package com.mauricio.apimonitoring.dto

import java.time.LocalDateTime

data class ApiError(
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
