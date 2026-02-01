package com.mauricio.apimonitoring.dto

import com.mauricio.apimonitoring.domain.HeaderEmbeddable
import com.mauricio.apimonitoring.domain.ParamsEmbeddable
import com.mauricio.apimonitoring.enum.HttpMethodEnum
import java.util.UUID

class MonitoredApiResponse(
    val id: UUID? = null,
    val name: String,
    val url: String,
    val method: HttpMethodEnum,
    val headers: MutableList<HeaderEmbeddable>,
    val params: MutableList<ParamsEmbeddable>,
    val responsibleEmails: MutableList<String>,
    val active: Boolean,
    val createdBy: String
)
