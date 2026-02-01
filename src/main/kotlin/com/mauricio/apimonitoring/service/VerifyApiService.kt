package com.mauricio.apimonitoring.service

import com.mauricio.apimonitoring.dto.ApiCheckHistoryRequest
import com.mauricio.apimonitoring.enum.StatusApiEnum
import com.mauricio.apimonitoring.exception.BusinessException
import com.mauricio.apimonitoring.repository.MonitoredApiRepository
import com.mauricio.apimonitoring.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.RequestPredicates.queryParam
import org.springframework.web.util.UriComponentsBuilder
import java.time.Duration

@Service
class VerifyApiService(
    private val apiRepository: MonitoredApiRepository,
    private val webClient: WebClient,
    private val apiCheckHistoryService: ApiCheckHistoryService
) {

    private val LOG = LoggerFactory.getLogger(UserService::class.java)

    private var statusCodeError: Int? = null

    fun checkExternalApi(url: String?) {

        val foundApi = apiRepository.findByUrl(url)
            .orElseThrow { BusinessException("API com url: ${url} não encontrada") }

        LOG.info("Iniciando check da API: ${foundApi.url}")

        val start = System.currentTimeMillis()

        try {

            val uri = UriComponentsBuilder
                .fromUriString(foundApi.url)
                .apply {
                    if (foundApi.params.isNotEmpty()) {
                        foundApi.params.forEach { param ->
                            queryParam(param.key, param.value)
                        }
                    }
                }
                .build(true)
                .toUri()

            val response = webClient
                .method(HttpMethod.valueOf(foundApi.method.name))
                .uri(uri)
                .headers { httpHeaders ->
                    if (foundApi.headers.isNotEmpty()) {
                        foundApi.headers.forEach {
                            httpHeaders.add(it.key, it.value)
                        }
                    }
                }
                .retrieve()
                .toBodilessEntity()
                .block(Duration.ofMillis(foundApi.timeoutMs.toLong()))

            val duration = System.currentTimeMillis() - start
            val status = response?.statusCode?.value()

            statusCodeError = response?.statusCode?.value()

            val isUp = status == foundApi.expectedStatus

            if (isUp) {
                LOG.info(
                    "API ${foundApi.url} respondeu com status $status em ${duration}ms"
                )
            } else {
                LOG.warn(
                    "API ${foundApi.url} respondeu $status em ${duration}ms, esperado ${foundApi.expectedStatus}"
                )
            }

            val savedHistory = ApiCheckHistoryRequest(
                apiId = foundApi.id.toString(),
                responseTimeMs = duration.toInt(),
                status = StatusApiEnum.UP,
                message = null,
                details = null
            )

            apiCheckHistoryService.create(savedHistory)

        } catch (ex: Exception) {
            val duration = System.currentTimeMillis() - start

            LOG.error(
                "Erro ao chamar API ${foundApi.url} após ${duration}ms - ${ex.message}"
            )

            val savedHistory = ApiCheckHistoryRequest(
                apiId = foundApi.id.toString(),
                responseTimeMs = duration.toInt(),
                status = StatusApiEnum.DOWN,
                message = ex.message,
                details = ex.stackTrace.toString()
            )

            apiCheckHistoryService.create(savedHistory)
        }


    }
}
