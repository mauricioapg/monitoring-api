//package com.mauricio.apimonitoring.config
//
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.http.HttpHeaders
//import org.springframework.http.MediaType
//import org.springframework.web.reactive.function.client.WebClient
//
//@Configuration
//class WebClientConfig {
//
//    @Bean
//    fun webClientBuilder(): WebClient.Builder =
//        WebClient.builder()
//
//    @Bean
//    fun webClient(builder: WebClient.Builder): WebClient =
//        builder
//            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//            .build()
//}

package com.mauricio.apimonitoring.config

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration

@Configuration
class WebClientConfig {

    @Value(value = "\${timeout.connect}")
    private val timeoutConnect: Int? = null

    @Value(value = "\${timeout.answer}")
    private val timeoutAnswer: Long? = null

    @Value(value = "\${timeout.read}")
    private val timeoutRead: Int? = null

    @Value(value = "\${timeout.write}")
    private val timeoutWrite: Int? = null

    @Bean
    fun webClientBuilder(): WebClient.Builder =
        WebClient.builder()

    @Bean
    fun webClient(builder: WebClient.Builder): WebClient {

        val httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutConnect) // tempo pra conectar (5s)
            .responseTimeout(Duration.ofSeconds(timeoutAnswer!!)) // tempo total de resposta
            .doOnConnected { conn ->
                conn
                    .addHandlerLast(ReadTimeoutHandler(timeoutRead!!)) // tempo de leitura
                    .addHandlerLast(WriteTimeoutHandler(timeoutWrite!!)) // tempo de escrita
            }

        return builder
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
    }
}