package com.depromeet.makers.domain.exception

open class MemberException(
    errorCode: ErrorCode,
) : DomainException(errorCode)

class MemberAlreadyExistsException : MemberException(ErrorCode.MEMBER_ALREADY_EXISTS)
class PassCordAlreadySetException : MemberException(ErrorCode.PASSCORD_ALREADY_SET)
class PassCordNotSetException : MemberException(ErrorCode.PASSCORD_NOT_SET)
class PassCordNotMatchedException : MemberException(ErrorCode.PASSCORD_NOT_MATCHED)
class MemberNotFoundException : MemberException(ErrorCode.MEMBER_NOT_FOUND)
