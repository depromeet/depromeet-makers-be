package com.depromeet.makers.presentation.restapi.config

import com.depromeet.makers.domain.exception.*
import com.depromeet.makers.domain.gateway.AlertGateway
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
import org.springframework.web.util.ContentCachingRequestWrapper
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.nio.charset.StandardCharsets

@RestControllerAdvice
class WebExceptionHandler(
    private val alertGateway: AlertGateway,
) {
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
        bodyCache: ContentCachingRequestWrapper,
    ): ResponseEntity<ErrorResponse> {
        if (exception is IOException) { //클라이언트에서 끊어버린 경우
            return ResponseEntity
                .internalServerError()
                .build()
        }

        val bodyStr = String(bodyCache.contentAsByteArray, StandardCharsets.UTF_8)
        alertGateway.sendError(
            "에러 메세지" to (exception.message ?: "Unknown error"),
            "요청 전문" to dumpRequest(request, bodyStr),
            "에러 StackTrace" to getStackTraceAsString(exception)
        )

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

    private fun dumpRequest(request: HttpServletRequest, bodyStr: String): String {
        val dump = StringBuilder("HttpRequest Dump:")
            .append("\n  Remote Addr   : ").append(request.remoteAddr)
            .append("\n  Protocol      : ").append(request.protocol)
            .append("\n  Request Method: ").append(request.method)
            .append("\n  Request URI   : ").append(request.requestURI)
            .append("\n  Query String  : ").append(request.queryString)
            .append("\n  Parameters    : ")

        val parameterNames = request.parameterNames
        while (parameterNames.hasMoreElements()) {
            val name = parameterNames.nextElement()
            dump.append("\n    ").append(name).append('=')
            val parameterValues = request.getParameterValues(name)
            for (value in parameterValues) {
                dump.append(value)
            }
        }

        dump.append("\n  Headers       : ")
        val headerNames = request.headerNames
        while (headerNames.hasMoreElements()) {
            val name = headerNames.nextElement()
            dump.append("\n    ").append(name).append(":")
            val headerValues = request.getHeaders(name)
            while (headerValues.hasMoreElements()) {
                dump.append(headerValues.nextElement())
            }
        }

        dump.append("\n==================== [ Body ] ====================")
        try {
            dump.append("\n").append(request.reader.use { it.readText() })
        } catch (ex: Exception) {
            dump.append("\n").append(bodyStr)
        }
        dump.append("\n==================== [ Body ] ====================")

        return dump.toString()
    }

    private fun getStackTraceAsString(throwable: Throwable): String {
        val stringWriter = StringWriter()
        throwable.printStackTrace(PrintWriter(stringWriter))
        return stringWriter.toString()
    }
}

