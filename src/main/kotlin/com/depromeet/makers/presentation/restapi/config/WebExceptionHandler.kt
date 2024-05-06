package com.depromeet.makers.presentation.restapi.config

import com.depromeet.makers.domain.exception.*
import com.depromeet.makers.presentation.restapi.dto.response.ErrorResponse
import com.depromeet.makers.util.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.util.BindErrorUtils

@RestControllerAdvice
class WebExceptionHandler {
    val logger = logger()

    @ExceptionHandler(value = [DomainException::class])
    fun handleDomainException(
        exception: DomainException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .badRequest()
            .body(exception.toErrorResponse())
    }

    @ExceptionHandler(
        value = [
            MethodArgumentNotValidException::class,
            MethodArgumentTypeMismatchException::class,
            HttpMessageNotReadableException::class,
            ConstraintViolationException::class,
        ]
    )
    fun handleInvalidInput(
        exception: Exception,
    ): ResponseEntity<ErrorResponse> {
        val data: Any? = when (exception) {
            is MethodArgumentNotValidException -> BindErrorUtils.resolve(exception.allErrors).values
            is MethodArgumentTypeMismatchException -> exception.localizedMessage
            is HttpMessageNotReadableException -> exception.localizedMessage
            else -> null
        }
        return ResponseEntity
            .badRequest()
            .body(
                ErrorResponse.fromErrorCode(
                    errorCode = ErrorCode.INVALID_INPUT,
                    data = data,
                )
            )
    }

    @ExceptionHandler(value = [Throwable::class])
    fun handleUnhandledException(
        exception: Throwable,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        logger.error("[UnhandledException] " + exception.stackTraceToString())
        return ResponseEntity
            .badRequest()
            .body(ErrorResponse.fromErrorCode(ErrorCode.UNKNOWN_SERVER_ERROR))
    }

    @ExceptionHandler(value = [AuthenticationException::class])
    fun handleAuthenticationException(
        exception: AuthenticationException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(exception.toErrorResponse())
    }

    @ExceptionHandler(value = [AuthorizationException::class])
    fun handleAuthorizationException(
        exception: AuthorizationException,
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(exception.toErrorResponse())
    }

    @ExceptionHandler(value = [AccessDeniedException::class])
    fun handleAccessDeniedException() = handleAuthorizationException(PermissionDeniedException())

    private fun DomainException.toErrorResponse() = ErrorResponse.fromErrorCode(
        errorCode = errorCode,
        data = data,
    )
}

