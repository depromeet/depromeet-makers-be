package com.depromeet.makers.config.filters

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

@Component
class LoggingFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val wrappedRequest = ContentCachingRequestWrapper(request)
        val wrappedResponse = ContentCachingResponseWrapper(response)

        logRequest(wrappedRequest)
        filterChain.doFilter(wrappedRequest, wrappedResponse)
        logResponse(wrappedResponse)

        wrappedResponse.copyBodyToResponse()
    }

    private fun logRequest(request: ContentCachingRequestWrapper) {
        val requestBody = String(request.contentAsByteArray, Charsets.UTF_8)

        logger.info(
            """
            =========================== [REQUEST] ===========================
            ▶ Method: ${request.method}
            ▶ URI: ${request.requestURI}
            ▶ Headers: ${request.headerNames.toList().joinToString { "$it: ${request.getHeader(it)}" }}
            ▶ Body: $requestBody
            =================================================================
            """,
        )
    }

    private fun logResponse(response: ContentCachingResponseWrapper) {
        val responseBody = String(response.contentAsByteArray, Charsets.UTF_8)

        logger.info(
            """
            =========================== [RESPONSE] ==========================
            ▶ Status: ${response.status}
            ▶ Headers: ${response.headerNames.joinToString { "$it: ${response.getHeader(it)}" }}
            ▶ Body: $responseBody
            =================================================================
            """,
        )
    }
}
