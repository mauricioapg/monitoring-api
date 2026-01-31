package com.mauricio.apimonitoring.dto

import com.mauricio.apimonitoring.domain.HeaderEmbeddable
import java.util.UUID

class MonitoredApiResponse(
    val id: UUID,
    val name: String,
    val url: String,
    val method: String,
    val headers: MutableList<HeaderEmbeddable>,
    val active: Boolean,
    val createdBy: String
)
