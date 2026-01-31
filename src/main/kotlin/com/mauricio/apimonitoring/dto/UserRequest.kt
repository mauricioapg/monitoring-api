package com.mauricio.apimonitoring.dto

import jakarta.validation.constraints.*

class UserRequest(

    @field:NotBlank
    val alias: String,

    @field:NotBlank
    val email: String,

    @field:NotBlank
    val password: String
)
