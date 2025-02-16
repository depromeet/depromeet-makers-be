package com.depromeet.makers.domain.exception

open class SessionException : DomainException {
    constructor(errorCode: ErrorCode) : super(errorCode)
    constructor(errorCode: ErrorCode, data: Any?) : super(errorCode, data)
}

class SessionPlaceNotFoundException : SessionException(ErrorCode.INVALID_SESSION_PLACE)
class SessionAlreadyExistsException : SessionException(ErrorCode.SESSION_ALREADY_EXISTS)
class SessionNotFoundException : SessionException(ErrorCode.SESSION_NOT_FOUND)
class SessionInvalidException(data: Any?) : SessionException(ErrorCode.INVALID_SESSION, data)
