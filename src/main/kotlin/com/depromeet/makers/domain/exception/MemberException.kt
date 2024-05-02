package com.depromeet.makers.domain.exception

open class MemberException(
    errorCode: ErrorCode,
): DomainException(errorCode)
class MemberAlreadyExistsException: MemberException(ErrorCode.MEMBER_ALREADY_EXISTS)
class PassCordAlreadySetException: MemberException(ErrorCode.PASSCORD_ALREADY_SET)
