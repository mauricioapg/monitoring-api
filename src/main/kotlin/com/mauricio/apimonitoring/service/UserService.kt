package com.mauricio.apimonitoring.service

import com.mauricio.apimonitoring.domain.UserEntity
import com.mauricio.apimonitoring.dto.UserRequest
import com.mauricio.apimonitoring.dto.UserResponse
import com.mauricio.apimonitoring.exception.NotFoundException
import com.mauricio.apimonitoring.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun create(request: UserRequest): UserResponse {

        val api = UserEntity(
            alias = request.alias,
            email = request.email,
            password = request.password,
            createdAt = LocalDateTime.now()
        )

        val saved = userRepository.save(api)

        return toResponse(saved)
    }

    fun list(): List<UserResponse> =
        userRepository.findAll().map { toResponse(it) }

    fun getById(id: String): UserResponse {
        val user = userRepository.findById(UUID.fromString(id))
            .orElseThrow { NotFoundException("User not found") }
        return toResponse(user)
    }

    fun update(id: UUID, request: UserRequest): UserResponse {
        val user = userRepository.findById(id)
            .orElseThrow { NotFoundException("User not found") }

        user.alias = request.alias
        user.email = request.email
        user.password = request.password

        val updated = userRepository.save(user)

        return toResponse(updated)
    }

    fun delete(id: UUID) =
        userRepository.deleteById(id)

    private fun toResponse(entity: UserEntity) =
        UserResponse(
            id = entity.id!!,
            alias = entity.alias,
            email = entity.email,
            password = entity.password,
            createdAt = entity.createdAt
        )
}
