package com.mauricio.apimonitoring.controller

import com.mauricio.apimonitoring.dto.MonitoredApiRequest
import com.mauricio.apimonitoring.dto.MonitoredApiResponse
import com.mauricio.apimonitoring.service.MonitoredApiService
import com.mauricio.apimonitoring.service.VerifyApiService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
    @RequestMapping("/api/v1/verify-external-api")
class VerifyApiController(
    private val service: VerifyApiService
) {

    @GetMapping
    fun checkExternalApi(
        @RequestParam url: String
    ){
        return service.checkExternalApi(url)
    }
}
