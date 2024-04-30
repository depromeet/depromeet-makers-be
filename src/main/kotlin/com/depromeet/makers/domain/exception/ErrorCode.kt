package com.depromeet.makers.domain.exception

enum class ErrorCode(
    val code: String,
    val message: String,
) {
    UNKNOWN_SERVER_ERROR("DE0001", "알 수 없는 오류가 발생했습니다"),
    UNAUTHORIZED("DE0002", "인가되지 않은 접근입니다"),

    INVALID_INPUT("IE0001","입력값(바디 혹은 파라미터)가 누락되었습니다"),

    MEMBER_ALREADY_EXISTS("ME0001", "해당 이메일의 사용자가 이미 존재합니다")
}
