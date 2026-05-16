package com.mauricio.apimonitoring.service

import com.mauricio.apimonitoring.domain.UserEntity
import com.mauricio.apimonitoring.dto.UserRequest
import com.mauricio.apimonitoring.dto.UserResponse
import com.mauricio.apimonitoring.dto.UserResponseFull
import com.mauricio.apimonitoring.exception.BusinessException
import com.mauricio.apimonitoring.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun create(request: UserRequest): UserResponse {

        val userFound = userRepository.findByEmail(request.email)
        if (userFound != null) {
            throw BusinessException("Email: ${request.email} já está em uso")
        }

        val encodedPassword = passwordEncoder.encode(request.password)

        val user = UserEntity(
            alias = request.alias,
            email = request.email,
            password = encodedPassword,
            createdAt = LocalDateTime.now()
        )

        val saved = userRepository.save(user)

        return toResponse(saved)
    }

    fun list(pageable: Pageable): Page<UserResponse> =
        userRepository
            .findAll(pageable)
            .map { toResponse(it) }

    fun listSimple(): List<UserResponse> {
        return userRepository.findAll().map {
            UserResponse(
                id = it.id!!,
                alias = it.alias,
                email = it.email,
                createdAt = it.createdAt
            )
        }
    }

    fun getById(id: String): UserResponse {
        val user = userRepository.findById(UUID.fromString(id))
            .orElseThrow { BusinessException("Usuário com id: ${id} não encontrado") }
        return toResponse(user)
    }

    fun getByEmail(email: String): UserResponseFull {
        val user = userRepository.findByEmail(email)
        return toResponseFull(user)
    }

    fun update(id: UUID, request: UserRequest): UserResponse {
        val user = userRepository.findById(id)
            .orElseThrow { BusinessException("Usuário com id: ${id} não encontrado") }

        val encodedPassword = passwordEncoder.encode(request.password)

        user.alias = request.alias
        user.email = request.email
        user.password = encodedPassword

        val updated = userRepository.save(user)

        return toResponse(updated)
    }

    fun delete(id: UUID){
        val user = userRepository.findById(id)
            .orElseThrow { BusinessException("Usuário com id: ${id} não encontrado") }

        userRepository.delete(user)
    }

    private fun toResponse(entity: UserEntity?) =
        UserResponse(
            id = entity?.id!!,
            alias = entity.alias,
            email = entity.email,
            createdAt = entity.createdAt
        )

    private fun toResponseFull(entity: UserEntity?) =
        UserResponseFull(
            id = entity?.id!!,
            alias = entity.alias,
            email = entity.email,
            password = entity.password,
            createdAt = entity.createdAt
        )
}
