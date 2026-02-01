package com.mauricio.apimonitoring.controller

import com.mauricio.apimonitoring.dto.ApiCheckHistoryRequest
import com.mauricio.apimonitoring.dto.ApiCheckHistoryResponse
import com.mauricio.apimonitoring.service.ApiCheckHistoryService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
    @RequestMapping("/api/v1/check-history")
class ApiCheckHistoryController(
    private val service: ApiCheckHistoryService,
) {

    @PostMapping
    fun create(
        @RequestBody @Valid request: ApiCheckHistoryRequest
    ) = service.create(request)

    @GetMapping
    fun list(): List<ApiCheckHistoryResponse> =
        service.list()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): ApiCheckHistoryResponse =
        service.getById(id)

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody @Valid request: ApiCheckHistoryRequest
    ): ApiCheckHistoryResponse =
        service.update(id, request)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) =
        service.delete(id)
}
