package com.mauricio.apimonitoring.dto

import com.mauricio.apimonitoring.domain.HeaderEmbeddable
import jakarta.validation.constraints.*

class MonitoredApiRequest(

    @field:NotBlank
    val name: String,

    @field:NotBlank
    val url: String,

    @field:NotBlank
    val method: String,

    val headers: MutableList<HeaderEmbeddable> = mutableListOf(),

    @field:Min(100)
    val timeoutMs: Int,

    @field:Min(1)
    val intervalMinutes: Int,

    val expectedStatus: Int = 200,

    val active: Boolean = true,
)
