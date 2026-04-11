package com.mauricio.apimonitoring.service

import com.mauricio.apimonitoring.domain.MonitoredApiEntity
import com.mauricio.apimonitoring.dto.ApiCheckHistoryRequest
import com.mauricio.apimonitoring.dto.EmailRequest
import com.mauricio.apimonitoring.enum.StatusApiEnum
import com.mauricio.apimonitoring.exception.BusinessException
import com.mauricio.apimonitoring.repository.ApiCheckHistoryRepository
import com.mauricio.apimonitoring.repository.MonitoredApiRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import java.time.Duration

@Service
class VerifyApiService(
    private val apiRepository: MonitoredApiRepository,
    private val apiCheckHistoryRepository: ApiCheckHistoryRepository,
    private val webClient: WebClient,
    private val apiCheckHistoryService: ApiCheckHistoryService,
    private val emailService: EmailService
) {

    private val LOG = LoggerFactory.getLogger(UserService::class.java)

    private var statusCodeError: Int? = null

    fun checkExternalApi(url: String?) {

        val foundApi = apiRepository.findByUrl(url)
            .orElseThrow { BusinessException("API com url: ${url} não encontrada") }

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

            val isUp = status == foundApi.expectedStatus

            val savedHistory = ApiCheckHistoryRequest(
                apiId = foundApi.id.toString(),
                responseTimeMs = duration.toInt(),
                status = if (isUp) StatusApiEnum.UP else StatusApiEnum.DOWN,
                message = null,
                details = null
            )

            apiCheckHistoryService.create(savedHistory)

            // CHAMA SEMPRE DEPOIS DE SALVAR
            monitoringStatusAPI(foundApi)

            if (!isUp) {
                LOG.warn("API ${foundApi.url} respondeu $status em ${duration}ms, esperado ${foundApi.expectedStatus}")
            }

        } catch (ex: Exception) {

            val duration = System.currentTimeMillis() - start

            LOG.error("Erro ao chamar API ${foundApi.url} após ${duration}ms - ${ex.message}")

            val savedHistory = ApiCheckHistoryRequest(
                apiId = foundApi.id.toString(),
                responseTimeMs = duration.toInt(),
                status = StatusApiEnum.DOWN,
                message = ex.message,
                details = ex.stackTrace.toString()
            )

            apiCheckHistoryService.create(savedHistory)

            // TAMBÉM CHAMA NO ERRO
            monitoringStatusAPI(foundApi)
        }
    }

    fun monitoringStatusAPI(api: MonitoredApiEntity){

        LOG.info("Monitorando estabilidade das APIs...")

        val threshold = 3 // quantidade de falhas consecutivas pra considerar DOWN

        val lastRecords = apiCheckHistoryRepository.findTop10ByApiIdOrderByCheckedAtDesc(api.id)

        if (lastRecords.isEmpty()) {
            LOG.warn("Sem histórico para API ${api.name}")
            return
        }

        // pega apenas os X últimos
        val lastFailures = lastRecords.take(threshold)

        val isDownConsecutively = lastFailures.all {
            it.status == StatusApiEnum.DOWN
        }

        if (isDownConsecutively) {
            LOG.error("API ${api.name} caiu $threshold vezes consecutivas!")

            // Aqui você pode disparar email
            api.responsibleEmails.toTypedArray().forEach {
                emailService.sendEmail(
                    EmailRequest(
                        to = it,
                        subject = "Alerta: API ${api.name} está offline",
                        body = "A API ${api.name} caiu $threshold vezes consecutivas. Último status: $statusCodeError"
                    )
                )
            }
        } else {
            LOG.info("API ${api.name} está estável")
        }
    }
}
