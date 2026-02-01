package com.mauricio.apimonitoring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class ApiMonitoringApplication

fun main(args: Array<String>) {
	runApplication<ApiMonitoringApplication>(*args)
}
