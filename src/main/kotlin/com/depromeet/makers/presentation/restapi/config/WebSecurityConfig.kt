package com.depromeet.makers.presentation.restapi.config

import com.depromeet.makers.presentation.restapi.config.filter.JWTAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class WebSecurityConfig(
    private val jwtAuthenticationFilter: JWTAuthenticationFilter,
) {
    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain = httpSecurity
        .csrf { it.disable() }
        .securityMatcher()
        .securityMatcher("/**")
            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        .exceptionHandling {
            it.accessDeniedHandler { a, b, c->
                println("Oh.. ad..")
            }
        }
        .build()
}
