package com.ermilovgrad.ogorodmanager.config

import com.ermilovgrad.ogorodmanager.service.RestTemplateService
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.Connection
import reactor.netty.http.client.HttpClient
import reactor.netty.tcp.TcpClient
import java.time.Duration
import java.util.concurrent.TimeUnit

@Configuration
class OgorodmanagerConfig {

    @Bean
    fun restTemplate(): RestTemplate = RestTemplateBuilder()
        .setConnectTimeout(Duration.ofSeconds(10))
        .build()

    @Bean
    fun restTemplateService(
        restTemplate: RestTemplate,
    ): RestTemplateService = RestTemplateService(restTemplate)

    @Bean
    fun webClient(): WebClient = WebClient.builder()
        .clientConnector(
            ReactorClientHttpConnector(
                HttpClient.from(TcpClient
            .create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
            .doOnConnected { connection: Connection ->
                connection.addHandlerLast(ReadTimeoutHandler(10000, TimeUnit.MILLISECONDS))
                connection.addHandlerLast(WriteTimeoutHandler(10000, TimeUnit.MILLISECONDS))
            }))
        )
        .build()

//    @Bean
//    fun webClientService(
//        webClient: WebClient,
//        tattooServiceConfiguration: TattooServiceConfiguration
//    ): WebClientService = WebClientService(tattooServiceConfiguration, webClient)

}
