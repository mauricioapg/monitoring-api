package com.mauricio.apimonitoring.controller

import com.mauricio.apimonitoring.dto.PageResponseDTO
import com.mauricio.apimonitoring.dto.UserRequest
import com.mauricio.apimonitoring.dto.UserResponse
import com.mauricio.apimonitoring.dto.UserResponseFull
import com.mauricio.apimonitoring.service.UserService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
    fun list(pageable: Pageable): PageResponseDTO<UserResponse> {
        return service.list(pageable)
    }

    @GetMapping("/teste")
    fun list(): List<UserResponse> {
        return service.listSimple()
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): UserResponse =
        service.getById(id)

    @GetMapping("/email/{email}")
    fun getByEmail(@PathVariable email: String): UserResponseFull =
        service.getByEmail(email)

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
