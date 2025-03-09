package com.depromeet.makers.config

import com.depromeet.makers.domain.exception.DomainException
import com.depromeet.makers.presentation.web.dto.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class WebExceptionHandler {
    @ExceptionHandler(DomainException::class)
    fun handleDomainException(e: DomainException): ResponseEntity<ErrorResponse> {
        val errorCode = e.errorCode
        if (errorCode.isCriticalError()) {
            return handleUnhandledException(e)
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(e.errorCode.code, ""))
    }

    @ExceptionHandler
    fun handleUnhandledException(e: Exception): ResponseEntity<ErrorResponse> {
        e.printStackTrace()
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(0, ""))
    }
}
