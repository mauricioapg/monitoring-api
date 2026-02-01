package com.mauricio.apimonitoring.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class ParamsEmbeddable(
    @Column(name = "param_key", nullable = false)
    var key: String = "",

    @Column(name = "param_value", nullable = false)
    var value: String = ""
)
