package com.mauricio.apimonitoring.service

import com.mauricio.apimonitoring.domain.ApiCheckHistoryEntity
import com.mauricio.apimonitoring.domain.MonitoredApiEntity
import com.mauricio.apimonitoring.dto.ApiCheckHistoryRequest
import com.mauricio.apimonitoring.dto.ApiCheckHistoryResponse
import com.mauricio.apimonitoring.dto.MonitoredApiRequest
import com.mauricio.apimonitoring.dto.MonitoredApiResponse
import com.mauricio.apimonitoring.enum.HttpMethodEnum
import com.mauricio.apimonitoring.enum.StatusApiEnum
import com.mauricio.apimonitoring.exception.BusinessException
import com.mauricio.apimonitoring.exception.NotFoundException
import com.mauricio.apimonitoring.repository.ApiCheckHistoryRepository
import com.mauricio.apimonitoring.repository.MonitoredApiRepository
import com.mauricio.apimonitoring.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID
import kotlin.collections.map

@Service
class ApiCheckHistoryService(
    private val apiCheckHistoryRepository: ApiCheckHistoryRepository,
    private val apiRepository: MonitoredApiRepository,
) {

    fun create(request: ApiCheckHistoryRequest) {

        val apiFound = apiRepository.findById(UUID.fromString(request.apiId))

        if(apiFound.isPresent){
            val historyApi = ApiCheckHistoryEntity(
                api = apiFound!!.get(),
                responseTimeMs = request.responseTimeMs,
                status = request.status,
                message = request.message,
                details = request.details,
                checkedAt = LocalDateTime.now(),
            )

            apiCheckHistoryRepository.save(historyApi)
        }
    }

    fun list(): List<ApiCheckHistoryResponse> =
        apiCheckHistoryRepository.findAll().map { toResponse(it) }

    fun getById(id: String): ApiCheckHistoryResponse {
        val api = apiCheckHistoryRepository.findById(UUID.fromString(id))
            .orElseThrow { BusinessException("Histórico de API com id: ${id} não encontrado") }
        return toResponse(api)
    }

    fun update(id: UUID, request: ApiCheckHistoryRequest): ApiCheckHistoryResponse {
        val checkHistoryApi = apiCheckHistoryRepository.findById(id)
            .orElseThrow { BusinessException("Histórico de API com id: ${id} não encontrado") }

        checkHistoryApi.responseTimeMs = request.responseTimeMs
        checkHistoryApi.status = request.status
        checkHistoryApi.message = request.message
        checkHistoryApi.details = request.details

        val updated = apiCheckHistoryRepository.save(checkHistoryApi)

        return toResponse(updated)
    }

    fun delete(id: UUID){
        val checkHistoryApi = apiCheckHistoryRepository.findById(id)
            .orElseThrow { BusinessException("Histórico de API com id: ${id} não encontrado") }

        apiCheckHistoryRepository.delete(checkHistoryApi)
    }

    private fun toResponse(entity: ApiCheckHistoryEntity): ApiCheckHistoryResponse {

        return ApiCheckHistoryResponse(
            apiId = entity.api.id!!.toString(),
            responseTimeMs = entity.responseTimeMs,
            status = StatusApiEnum.valueOf(entity.status.name),
            message = entity.message,
            details = entity.details,
            checkedAt = entity.checkedAt
        )
    }
}
