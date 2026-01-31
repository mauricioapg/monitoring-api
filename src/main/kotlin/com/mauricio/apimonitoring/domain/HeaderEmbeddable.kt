package com.mauricio.apimonitoring.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class HeaderEmbeddable(
    @Column(name = "header_key", nullable = false)
    var key: String = "",

    @Column(name = "header_value", nullable = false)
    var value: String = ""
)
