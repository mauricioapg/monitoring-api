package com.mauricio.apimonitoring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiMonitoringApplication

fun main(args: Array<String>) {
	runApplication<ApiMonitoringApplication>(*args)
}
