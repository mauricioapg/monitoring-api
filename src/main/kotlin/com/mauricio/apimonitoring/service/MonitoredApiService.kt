package com.mauricio.apimonitoring.service

import com.mauricio.apimonitoring.domain.MonitoredApiEntity
import com.mauricio.apimonitoring.dto.MonitoredApiRequest
import com.mauricio.apimonitoring.dto.MonitoredApiResponse
import com.mauricio.apimonitoring.enum.HttpMethodEnum
import com.mauricio.apimonitoring.exception.BusinessException
import com.mauricio.apimonitoring.exception.NotFoundException
import com.mauricio.apimonitoring.repository.ApiCheckHistoryRepository
import com.mauricio.apimonitoring.repository.MonitoredApiRepository
import com.mauricio.apimonitoring.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID
import kotlin.collections.map

@Service
class MonitoredApiService(
    private val apiRepository: MonitoredApiRepository,
    private val apiCheckHistoryRepository: ApiCheckHistoryRepository,
    private val userRepository: UserRepository
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun create(userId: UUID, request: MonitoredApiRequest): MonitoredApiResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { BusinessException("Nenhum usuário não encontrado com id: $userId") }

        val apiFound = apiRepository.findByUrl(request.url)
        if (apiFound.isPresent) {
            throw BusinessException("API com url: ${request.url} já está em uso")
        }

        val api = MonitoredApiEntity(
            user = user,
            name = request.name,
            url = request.url,
            method = request.method,
            headers = request.headers,
            responsibleEmails = request.responsibleEmails,
            expectedStatus = request.expectedStatus,
            timeoutMs = request.timeoutMs,
            intervalMinutes = request.intervalMinutes,
            maxFailureThreshold = request.maxFailureThreshold,
            timeToSetOffline = request.timeToSetOffline,
            createdAt = LocalDateTime.now()
        )

        val saved = apiRepository.save(api)

        return toResponse(saved)
    }

    fun listAll(): List<MonitoredApiResponse> =
        apiRepository
            .findAll()
            .map { toResponse(it) }

    fun list(pageable: Pageable): Page<MonitoredApiResponse> =
        apiRepository
            .findAll(pageable)
            .map { toResponse(it) }

    fun getById(id: String): MonitoredApiResponse {
        val api = apiRepository.findById(UUID.fromString(id))
            .orElseThrow { BusinessException("API com id: ${id} não encontrada") }
        return toResponse(api)
    }

    fun update(id: UUID, request: MonitoredApiRequest): MonitoredApiResponse {
        val api = apiRepository.findById(id)
            .orElseThrow { BusinessException("API com id: ${id} não encontrada") }

        api.name = request.name
        api.url = request.url
        api.method = HttpMethodEnum.valueOf(request.method.name)
        api.headers = request.headers
        api.expectedStatus = request.expectedStatus
        api.timeoutMs = request.timeoutMs
        api.intervalMinutes = request.intervalMinutes
        api.timeToSetOffline = request.timeToSetOffline
        api.responsibleEmails = request.responsibleEmails
        api.active = request.active

        val updated = apiRepository.save(api)

        return toResponse(updated)
    }

    @Transactional
    fun delete(id: UUID) {
        val api = apiRepository.findById(id)
            .orElseThrow { BusinessException("API com id: $id não encontrada") }

        apiCheckHistoryRepository.deleteByApi(api)
        apiCheckHistoryRepository.flush() // ESSENCIAL PARA DELETAR OS HISTÓRICOS ANTES DE DELETAR A API

        apiRepository.delete(api)
    }

    private fun toResponse(entity: MonitoredApiEntity): MonitoredApiResponse {

        val userId = entity.user.id ?: throw NotFoundException("Id do usuário de criação está nulo")
        val user = userRepository.findById(userId)
            .orElseThrow { BusinessException("Nenhum usuário de criação encontrado com id: $userId") }

        return MonitoredApiResponse(
            id = entity.id!!,
            name = entity.name,
            url = entity.url,
            method = HttpMethodEnum.valueOf(entity.method.name),
            intervalMinutes = entity.intervalMinutes,
            maxFailureThreshold = entity.maxFailureThreshold,
            timeout = entity.timeoutMs,
            timeToSetOffline = entity.timeToSetOffline,
            headers = entity.headers,
            params = entity.params,
            responsibleEmails = entity.responsibleEmails,
            active = entity.active,
            createdBy = user.alias
        )
    }
}
