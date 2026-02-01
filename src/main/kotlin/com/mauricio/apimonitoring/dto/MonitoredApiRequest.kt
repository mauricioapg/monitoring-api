package com.mauricio.apimonitoring.dto

import com.mauricio.apimonitoring.domain.HeaderEmbeddable
import com.mauricio.apimonitoring.domain.ParamsEmbeddable
import com.mauricio.apimonitoring.enum.HttpMethodEnum
import jakarta.validation.constraints.*

class MonitoredApiRequest(

    @field:NotBlank
    val name: String,

    @field:NotBlank
    val url: String,

    @field:NotNull
    val method: HttpMethodEnum,

    val headers: MutableList<HeaderEmbeddable> = mutableListOf(),

    val params: MutableList<ParamsEmbeddable> = mutableListOf(),

    val responsibleEmails: MutableList<String> = mutableListOf(),

    @field:Min(100)
    val timeoutMs: Int,

    @field:Min(1)
    val intervalMinutes: Int,

    val expectedStatus: Int = 200,

    val active: Boolean = true,
)
