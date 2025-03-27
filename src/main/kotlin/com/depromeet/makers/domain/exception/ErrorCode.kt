package com.depromeet.makers.domain.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val httpStatus: HttpStatus,
    val code: Int,
    val message: String,
) {
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50000, "알 수 없는 오류가 발생했어요"),

    NOT_FOUND(HttpStatus.NOT_FOUND, 40000, "존재하지 않아요"),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 40100, "권한이 없어요"),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, 40101, "토큰이 없어요"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, 40102, "토큰이 만료되었어요"),
    TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, 40103, "토큰이 유효하지 않아요"),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, 40104, "인증되지 않았어요"),
    ;

    fun isCriticalError(): Boolean {
        return this.code >= 50000
    }
}
