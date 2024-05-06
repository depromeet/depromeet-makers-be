package com.depromeet.makers.presentation.restapi.config

import com.depromeet.makers.infrastructure.token.JWTTokenProvider
import com.depromeet.makers.presentation.restapi.config.filter.JWTAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.HandlerExceptionResolver

@Configuration
@EnableMethodSecurity
class WebSecurityConfig {
    @Bean
    @Order(0)
    fun securityFilterChain(
        httpSecurity: HttpSecurity,
        jwtTokenProvider: JWTTokenProvider,
        handlerExceptionResolver: HandlerExceptionResolver,
    ): SecurityFilterChain = httpSecurity
        .securityMatcher("/v1/members/**")
        .csrf { it.disable() }
        .sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        .authorizeHttpRequests {
            it.anyRequest().authenticated()
        }
        .addFilterBefore(
            JWTAuthenticationFilter(jwtTokenProvider, handlerExceptionResolver),
            UsernamePasswordAuthenticationFilter::class.java
        )
        .exceptionHandling {
            it.accessDeniedHandler { a, b, c ->
                println("Oh.. ad..")
            }.authenticationEntryPoint { request, response, authException ->
                println("Oh,, aed")
            }
        }
        .build()

    @Bean
    @Order(0)
    fun authSecurityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain = httpSecurity
        .securityMatcher("/v1/auth/**", "/v1/members")
        .csrf { it.disable() }
        .sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        .authorizeHttpRequests {
            it.anyRequest().permitAll()
        }
        .build()
}
