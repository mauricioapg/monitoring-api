package com.mauricio.apimonitoring.dto

import com.mauricio.apimonitoring.enum.StatusApiEnum
import jakarta.validation.constraints.*
import java.time.LocalDateTime

class ApiCheckHistoryResponse(

    val apiId: String,

    @field:Min(100)
    val responseTimeMs: Int? = null,

    val status: StatusApiEnum,

    @field:NotBlank
    val message: String? = null,

    @field:NotBlank
    val details: String? = null,

    val checkedAt: LocalDateTime
)
