package com.depromeet.makers.presentation.restapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class WebSecurityConfig {
    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain = httpSecurity
        .csrf { it.disable() }
        .authorizeHttpRequests {
            it
                .requestMatchers("/error").permitAll()
                .anyRequest().permitAll()
        }
        .build()
}
