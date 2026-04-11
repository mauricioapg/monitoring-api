package com.mauricio.apimonitoring.dto

import jakarta.validation.constraints.*

class EmailRequest(

    @field:NotBlank
    val to: String,

    @field:NotBlank
    val subject: String,

    @field:NotBlank
    val body: String
)
