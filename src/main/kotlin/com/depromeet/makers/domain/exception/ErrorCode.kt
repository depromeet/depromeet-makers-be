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
    UNKNOWN_RESOURCE("DE0004", "해당 리소스를 찾을 수 없습니다"),
    INVALID_METHOD("DE0005", "요청 메서드가 잘못되었습니다. API 문서를 확인하세요"),

    /**
     * 인증(Authentication) 관련 오류
     * @see com.depromeet.makers.domain.exception.AuthenticationException
     */
    TOKEN_NOT_PROVIDED("AU0001", "인증 토큰이 누락되었습니다."),
    TOKEN_NOT_VALID("AU0002", "인증 토큰 형태가 올바르지 않습니다."),
    TOKEN_EXPIRED("AU0003", "인증 토큰이 만료되었습니다"),

    /**
     * 인가(Authorization) 관련 오류
     * @see com.depromeet.makers.domain.exception.AuthorizationException
     */
    PERMISSION_DENIED("AT0001", "해당 리소스에 대한 권한이 없습니다."),

    /**
     * 사용자 관련 오류
     * @see com.depromeet.makers.domain.exception.MemberException
     */
    MEMBER_ALREADY_EXISTS("ME0001", "해당 이메일의 사용자가 이미 존재합니다"),
    PASSCORD_ALREADY_SET("ME0002", "이미 비밀번호가 지정되었습니다. 관리자에게 문의하세요"),
    PASSCORD_NOT_SET("ME0003", "비밀번호가 설정되지 않았습니다. 먼저 설정하세요"),
    PASSCORD_NOT_MATCHED("ME0004", "비밀번호가 일치하지 않습니다"),
    MEMBER_NOT_FOUND("ME0005", "사용자를 찾을 수 없습니다"),

    /**
     * 세션 관련 오류
     * @see com.depromeet.makers.domain.exception.SessionException
     */
    INVALID_SESSION_PLACE("SE0001", "오프라인 세션의 장소가 기입이 필요합니다"),
    SESSION_ALREADY_EXISTS("SE0002", "이미 해당 세션이 존재합니다"),
    SESSION_NOT_FOUND("SE0003", "해당하는 세션을 찾을 수 없습니다"),

    /**
     * 출석 관련 오류
     * @see com.depromeet.makers.domain.exception.AttendanceException
     */
    BEFORE_AVAILABLE_ATTENDANCE_TIME("AT0001", "출석 시간 이전입니다"),
    AFTER_AVAILABLE_ATTENDANCE_TIME("AT0002", "출석, 지각 가능 시간이 지났습니다"),
    ALREADY_ATTENDANCE("AT0003", "이미 출석 처리 되었습니다."),
    INVALID_CHECKIN_TIME("AT0004", "현재 주차에 해당하는 세션을 찾을 수 없습니다."),
    MISSING_PLACE_PARAM("AT0005", "오프라인 세션 출석체크의 현재 위치 정보가 누락되었습니다."),
    INVALID_CHECKIN_DISTANCE("AT0006", "현재 위치와 세션 장소의 거리가 너무 멉니다."),
    NOT_EXIST_ATTENDANCE("AT0007", "해당하는 출석 정보가 없습니다."),
    INVALID_CHECKIN_CODE("AT0008", "잘못된 출석 코드입니다. 다시 시도해주세요."),
    NOT_SUPPORTED_CHECKIN_CODE("AT0009", "출석 코드로 출석할 수 없는 세션입니다."),
    TRY_COUNT_OVER("AT0010", "출석 인증 횟수를 초과했습니다."),
}
