package com.depromeet.makers.domain.exception

open class AuthenticationException(
    errorCode: ErrorCode,
) : DomainException(errorCode)

class AuthenticationTokenNotFoundException: AuthenticationException(ErrorCode.TOKEN_NOT_PROVIDED)
class AuthenticationTokenNotValidException: AuthenticationException(ErrorCode.TOKEN_NOT_VALID)
