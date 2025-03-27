package com.depromeet.makers.config

import com.depromeet.makers.domain.exception.DomainException
import com.depromeet.makers.domain.exception.ErrorCode
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

        return ResponseEntity.status(errorCode.httpStatus)
            .body(ErrorResponse(e.errorCode.code, e.errorCode.message))
    }

    @ExceptionHandler
    fun handleUnhandledException(e: Exception): ResponseEntity<ErrorResponse> {
        e.printStackTrace()
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(ErrorCode.INTERNAL_ERROR.code, ErrorCode.INTERNAL_ERROR.message))
    }
}
