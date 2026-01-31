package com.mauricio.apimonitoring.controller

import com.mauricio.apimonitoring.dto.MonitoredApiRequest
import com.mauricio.apimonitoring.dto.MonitoredApiResponse
import com.mauricio.apimonitoring.service.MonitoredApiService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
    @RequestMapping("/api/v1/monitored-apis")
class MonitoredApiController(
    private val service: MonitoredApiService
) {

    @PostMapping("/user/{userId}")
    fun create(
        @PathVariable userId: UUID,
        @RequestBody @Valid request: MonitoredApiRequest
    ): MonitoredApiResponse =
        service.create(userId, request)

    @GetMapping
    fun list(): List<MonitoredApiResponse> =
        service.list()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): MonitoredApiResponse =
        service.getById(id)

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody @Valid request: MonitoredApiRequest
    ): MonitoredApiResponse =
        service.update(id, request)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) =
        service.delete(id)
}
