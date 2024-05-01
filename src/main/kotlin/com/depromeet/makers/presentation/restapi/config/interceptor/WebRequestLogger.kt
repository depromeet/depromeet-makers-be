package com.depromeet.makers.presentation.restapi.config.interceptor

import com.depromeet.makers.util.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class WebRequestLogger: HandlerInterceptor {
    val startTimeAttr = "startTime"
    val logger = logger()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val startTime = System.currentTimeMillis()
        request.setAttribute(startTimeAttr, startTime)
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?,
    ) {
        val startTime = request.getAttribute(startTimeAttr) as Long
        val endTime = System.currentTimeMillis()
        val executionTime = endTime - startTime

        val userAgent = request.getHeader("User-Agent")
        val originIp = request.getHeader("X-FORWARDED-FOR")
                ?: request.getHeader("CF-Connecting-IP")
                ?: request.remoteAddr

        if (ex == null){
            logger.info(
                "{} {} {} {} {}ms {}",
                request.method,
                request.requestURI,
                originIp,
                response.status,
                executionTime,
                userAgent,
            )
        } else {

        }

    }
}
