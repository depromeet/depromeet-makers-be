package com.depromeet.makers.domain.exception

open class DefaultException(
    errorCode: ErrorCode,
): DomainException(errorCode)

class UnknownErrorException: DefaultException(ErrorCode.UNKNOWN_SERVER_ERROR)
class UnauthorizedException: DefaultException(ErrorCode.UNAUTHORIZED)
class InvalidInputException: DefaultException(ErrorCode.INVALID_INPUT)
