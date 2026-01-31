package com.mauricio.apimonitoring.service

import com.mauricio.apimonitoring.exception.BusinessException
import com.mauricio.apimonitoring.repository.MonitoredApiRepository
import com.mauricio.apimonitoring.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.time.Duration

@Service
class VerifyApiService(
    private val apiRepository: MonitoredApiRepository,
    private val webClient: WebClient
) {

    private val LOG = LoggerFactory.getLogger(UserService::class.java)

    fun checkExternalApi(url: String?) {

        val foundApi = apiRepository.findByUrl(url)
            .orElseThrow { BusinessException("API com url: ${url} não encontrada") }

        LOG.info("Iniciando check da API: ${foundApi.url}")

        val start = System.currentTimeMillis()

        try {
            val response = webClient
                .method(HttpMethod.valueOf(foundApi.method.name))
                .uri(foundApi.url)
                .headers { httpHeaders ->
                    foundApi.headers.forEach {
                        httpHeaders.add(it.key, it.value)
                    }
                }
                .retrieve()
                .toBodilessEntity()
                .block(Duration.ofMillis(foundApi.timeoutMs.toLong()))

            val duration = System.currentTimeMillis() - start
            val status = response?.statusCode?.value()

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

            // 🔜 salvar histórico:
            // status = UP / DOWN
            // httpStatus = status
            // responseTime = duration

        } catch (ex: Exception) {
            val duration = System.currentTimeMillis() - start

            LOG.error(
                "Erro ao chamar API ${foundApi.url} após ${duration}ms - ${ex.message}"
            )

            // 🔜 salvar histórico:
            // status = DOWN
            // errorMessage = ex.message
            // responseTime = duration
        }


    }
}
