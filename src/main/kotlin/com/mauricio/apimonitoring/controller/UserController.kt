package com.mauricio.apimonitoring.controller

import com.mauricio.apimonitoring.dto.UserRequest
import com.mauricio.apimonitoring.dto.UserResponse
import com.mauricio.apimonitoring.service.UserService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val service: UserService,
) {

    @PostMapping
    fun create(
        @RequestBody @Valid request: UserRequest
    ): UserResponse =
        service.create(request)

    @GetMapping
    fun list(): List<UserResponse> =
        service.list()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): UserResponse =
        service.getById(id)

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody @Valid request: UserRequest
    ): UserResponse =
        service.update(id, request)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) =
        service.delete(id)
}
