package com.mauricio.apimonitoring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Bean
    fun webClientBuilder(): WebClient.Builder =
        WebClient.builder()

    @Bean
    fun webClient(builder: WebClient.Builder): WebClient =
        builder
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
}
