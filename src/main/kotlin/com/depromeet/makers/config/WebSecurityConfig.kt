package com.depromeet.makers.config

import com.depromeet.makers.components.JWTTokenProvider
import com.depromeet.makers.config.filters.JWTAuthenticationFilter
import com.depromeet.makers.domain.exception.DomainException
import com.depromeet.makers.domain.exception.ErrorCode
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.HandlerExceptionResolver

@Configuration
class WebSecurityConfig {
    @Bean
    @Order(0)
    fun authSecurityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain =
        httpSecurity
            .securityMatcher("/v1/auth/**")
            .csrf { it.disable() }
            .cors(Customizer.withDefaults())
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                it.anyRequest().permitAll()
            }
            .build()

    @Bean
    @Order(1)
    fun securityFilterChain(
        httpSecurity: HttpSecurity,
        jwtTokenProvider: JWTTokenProvider,
        handlerExceptionResolver: HandlerExceptionResolver,
    ): SecurityFilterChain = httpSecurity
        .securityMatcher("/**")
        .csrf { it.disable() }
        .cors(Customizer.withDefaults())
        .sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        .authorizeHttpRequests {
            it.anyRequest().authenticated()
        }
        .addFilterBefore(
            JWTAuthenticationFilter(jwtTokenProvider, handlerExceptionResolver),
            UsernamePasswordAuthenticationFilter::class.java,
        )
        .exceptionHandling {
            it.accessDeniedHandler { request, response, exception ->
                handlerExceptionResolver.resolveException(
                    request,
                    response,
                    null,
                    DomainException(ErrorCode.UNAUTHORIZED),
                )
            }.authenticationEntryPoint { request, response, authException ->
                handlerExceptionResolver.resolveException(
                    request,
                    response,
                    null,
                    DomainException(ErrorCode.UNAUTHENTICATED),
                )
            }
        }
        .build()
}
