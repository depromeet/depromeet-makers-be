package com.depromeet.makers.config.filters

import com.depromeet.makers.components.JWTTokenProvider
import com.depromeet.makers.domain.exception.DomainException
import com.depromeet.makers.domain.exception.ErrorCode
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

class JWTAuthenticationFilter(
    private val jwtTokenProvider: JWTTokenProvider,
    private val handlerExceptionResolver: HandlerExceptionResolver,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val authenticationHeader =
                request.getHeader("Authorization") ?: throw DomainException(ErrorCode.TOKEN_NOT_FOUND)
            val accessToken = if (authenticationHeader.startsWith("Bearer ")) {
                authenticationHeader.substring(7)
            } else {
                throw DomainException(ErrorCode.TOKEN_NOT_VALID)
            }

            val authentication = jwtTokenProvider.parseAuthentication(accessToken)
            SecurityContextHolder.getContext().authentication = authentication
            return filterChain.doFilter(request, response)
        } catch (e: Exception) {
            // Exception Handler로 넘김
            handlerExceptionResolver.resolveException(request, response, null, e)
        }
    }
}
