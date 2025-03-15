package com.depromeet.makers.config

import com.depromeet.makers.repository.client.AppleAuthClient
import com.depromeet.makers.repository.client.KakaoAuthClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    @Bean
    fun restClient(): RestClient {
        return RestClient.create()
    }

    @Bean
    fun appleAuthClient(restClient: RestClient): AppleAuthClient {
        val restClientAdapter = RestClientAdapter.create(restClient)
        return HttpServiceProxyFactory
            .builderFor(restClientAdapter)
            .build()
            .createClient(AppleAuthClient::class.java)
    }

    @Bean
    fun kakaoAuthClient(restClient: RestClient): KakaoAuthClient {
        val restClientAdapter = RestClientAdapter.create(restClient)
        return HttpServiceProxyFactory
            .builderFor(restClientAdapter)
            .build()
            .createClient(KakaoAuthClient::class.java)
    }
}
