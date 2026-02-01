package com.mauricio.apimonitoring.dto

import com.mauricio.apimonitoring.enum.StatusApiEnum
import jakarta.validation.constraints.*

class ApiCheckHistoryRequest(

    val apiId: String,

    val responseTimeMs: Int,

    @field:NotNull
    val status: StatusApiEnum,

    val message: String? = null,

    val details: String? = null,
)
