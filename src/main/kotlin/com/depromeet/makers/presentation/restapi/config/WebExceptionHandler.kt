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
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.resource.NoResourceFoundException
import org.springframework.web.util.BindErrorUtils
import java.io.IOException

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

    @ExceptionHandler(
        value = [
            NoResourceFoundException::class,
            HttpRequestMethodNotSupportedException::class,
        ]
    )
    fun handleUnknownResource(
        exception: Exception,
    ): ResponseEntity<ErrorResponse> {
        val errorCode = when(exception) {
            is NoResourceFoundException -> ErrorCode.UNKNOWN_RESOURCE
            is HttpRequestMethodNotSupportedException -> ErrorCode.INVALID_METHOD
            else -> ErrorCode.UNKNOWN_SERVER_ERROR
        }
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                ErrorResponse.fromErrorCode(
                    errorCode = errorCode,
                )
            )
    }

    @ExceptionHandler(value = [Throwable::class])
    fun handleUnhandledException(
        exception: Throwable,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        if (exception is IOException) { //클라이언트에서 끊어버린 경우
            return ResponseEntity
                .internalServerError()
                .build()
        }

        logger.error("[UnhandledException] " + exception.stackTraceToString())
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
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

