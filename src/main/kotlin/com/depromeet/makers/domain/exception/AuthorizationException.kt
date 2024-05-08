package com.depromeet.makers.domain.exception

open class AuthorizationException(
    errorCode: ErrorCode,
) : DomainException(errorCode)

class PermissionDeniedException : AuthorizationException(ErrorCode.PERMISSION_DENIED)
