package com.mauricio.apimonitoring.service

import com.mauricio.apimonitoring.domain.MonitoredApiEntity
import com.mauricio.apimonitoring.dto.MonitoredApiRequest
import com.mauricio.apimonitoring.dto.MonitoredApiResponse
import com.mauricio.apimonitoring.exception.NotFoundException
import com.mauricio.apimonitoring.repository.MonitoredApiRepository
import com.mauricio.apimonitoring.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID
import kotlin.collections.map

@Service
class MonitoredApiService(
    private val apiRepository: MonitoredApiRepository,
    private val userRepository: UserRepository
) {

    fun create(userId: UUID, request: MonitoredApiRequest): MonitoredApiResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { NotFoundException("User not found") }

        val api = MonitoredApiEntity(
            user = user,
            name = request.name,
            url = request.url,
            method = request.method,
            headers = request.headers,
            expectedStatus = request.expectedStatus,
            timeoutMs = request.timeoutMs,
            intervalMinutes = request.intervalMinutes,
            createdAt = LocalDateTime.now(),
            createdBy = user.id.toString()
        )

        val saved = apiRepository.save(api)

        return toResponse(saved)
    }

    fun list(): List<MonitoredApiResponse> =
        apiRepository.findAll().map { toResponse(it) }

    fun getById(id: String): MonitoredApiResponse {
        val api = apiRepository.findById(UUID.fromString(id))
            .orElseThrow { NotFoundException("Monitored API not found") }
        return toResponse(api)
    }

    fun update(id: UUID, request: MonitoredApiRequest): MonitoredApiResponse {
        val api = apiRepository.findById(id)
            .orElseThrow { NotFoundException("Monitored API not found") }

        api.name = request.name
        api.url = request.url
        api.method = request.method
        api.headers = request.headers
        api.expectedStatus = request.expectedStatus
        api.timeoutMs = request.timeoutMs
        api.intervalMinutes = request.intervalMinutes
        api.active = request.active

        val updated = apiRepository.save(api)

        return toResponse(updated)
    }

    fun delete(id: UUID) =
        apiRepository.deleteById(id)

    private fun toResponse(entity: MonitoredApiEntity) =
        MonitoredApiResponse(
            id = entity.id!!,
            name = entity.name,
            url = entity.url,
            method = entity.method,
            headers = entity.headers,
            active = entity.active,
            createdBy = userRepository.getById(UUID.fromString(entity.createdBy)).alias
        )
}
