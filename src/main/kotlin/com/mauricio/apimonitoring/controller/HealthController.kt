package com.mauricio.apimonitoring.controller

import com.mauricio.apimonitoring.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/health")
class HealthController(
    private val service: UserService,
) {

    @GetMapping
    fun health() = "OK"
}
