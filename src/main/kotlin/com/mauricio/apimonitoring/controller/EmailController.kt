package com.mauricio.apimonitoring.controller

import com.mauricio.apimonitoring.dto.EmailRequest
import com.mauricio.apimonitoring.dto.UserRequest
import com.mauricio.apimonitoring.dto.UserResponse
import com.mauricio.apimonitoring.service.EmailService
import com.mauricio.apimonitoring.service.UserService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/email")
class EmailController(
    private val service: EmailService,
) {

    @PostMapping
    fun send(
        @RequestBody @Valid request: EmailRequest
    ){
        service.sendEmail(request)
    }
}
