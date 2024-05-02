package com.depromeet.makers.presentation.restapi.config

import com.depromeet.makers.presentation.restapi.config.interceptor.WebRequestLogger
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val webRequestLogger: WebRequestLogger,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(webRequestLogger)
    }
}
