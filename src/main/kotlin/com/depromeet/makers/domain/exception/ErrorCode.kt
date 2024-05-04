package com.depromeet.makers.domain.exception

enum class ErrorCode(
    val code: String,
    val message: String,
) {
    /**
     * 기본 오류
     * @see com.depromeet.makers.domain.exception.DefaultException
     */
    UNKNOWN_SERVER_ERROR("DE0001", "알 수 없는 오류가 발생했습니다"),
    UNAUTHORIZED("DE0002", "인가되지 않은 접근입니다"),
    INVALID_INPUT("DE0003", "입력값(바디 혹은 파라미터)가 누락되었습니다"),

    /**
     * 사용자 관련 오류
     * @see com.depromeet.makers.domain.exception.MemberException
     */
    MEMBER_ALREADY_EXISTS("ME0001", "해당 이메일의 사용자가 이미 존재합니다"),
    PASSCORD_ALREADY_SET("ME0002", "이미 비밀번호가 지정되었습니다. 관리자에게 문의하세요"),
    PASSCORD_NOT_SET("ME0003", "비밀번호가 설정되지 않았습니다. 먼저 설정하세요"),
    MEMBER_NOT_FOUND("ME0004", "사용자를 찾을 수 없습니다"),

    /**
     * 세션 관련 오류
     * @see com.depromeet.makers.domain.exception.SessionException
     */
    INVALID_SESSION_PLACE("SE0001", "오프라인 세션의 장소가 기입이 필요합니다"),
    SESSION_ALREADY_EXISTS("SE0002", "이미 해당 세션이 존재합니다"),
}
