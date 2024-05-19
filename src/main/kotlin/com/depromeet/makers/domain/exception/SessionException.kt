package com.depromeet.makers.domain.exception

open class SessionException(
    errorCode: ErrorCode,
) : DomainException(errorCode)

class SessionPlaceNotFoundException : SessionException(ErrorCode.INVALID_SESSION_PLACE)
class SessionAlreadyExistsException : SessionException(ErrorCode.SESSION_ALREADY_EXISTS)
class SessionNotFoundException : SessionException(ErrorCode.SESSION_NOT_FOUND)
