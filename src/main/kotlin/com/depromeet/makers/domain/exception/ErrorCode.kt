package com.depromeet.makers.domain.exception

enum class ErrorCode(
    val code: Int,
    val message: String,
) {
    INTERNAL_ERROR(5000, "알 수 없는 오류가 발생했어요"),
    MEMBER_NOT_FOUND(4000, "테스트를 찾을 수 없어요"),
    TOKEN_EXPIRED(4001, "토큰이 만료되었어요"),
    TOKEN_NOT_FOUND(4002, "토큰이 없어요"),
    TOKEN_NOT_VALID(4003, "토큰이 유효하지 않아요"),
    KAKAO_LOGIN_FAILED(4002, "카카오 로그인에 실패했어요"),
    UNAUTHORIZED(4003, "권한이 없어요"),
    UNAUTHENTICATED(4004, "인증되지 않았어요"),
    ;

    fun isCriticalError(): Boolean {
        return this.code >= 5000
    }
}
