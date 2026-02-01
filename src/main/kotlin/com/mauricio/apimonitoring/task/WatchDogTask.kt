package com.mauricio.apimonitoring.task

import com.mauricio.apimonitoring.service.MonitoredApiService
import com.mauricio.apimonitoring.service.VerifyApiService
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class WatchDogTask @Autowired constructor(
    private val verifyApiService: VerifyApiService,
    private val monitoredApiService: MonitoredApiService
){

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Value(value = "\${task.watchdog.monitoring.enabled}")
    private val taskEnabled: Boolean? = null

    @Transactional
    @Scheduled(fixedDelay = 5000)
    fun run() {

        if( taskEnabled == false )
            return

        val apiUrls = monitoredApiService.list().filter { it.active }

        if(apiUrls.isNotEmpty()){
            apiUrls.forEach { api ->
                verifyApiService.checkExternalApi(api.url)
            }
        }

        logger.info("Verificação de APIs finalizada")
        logger.info(" ")

    }

}