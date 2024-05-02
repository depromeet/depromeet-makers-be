package com.depromeet.makers.domain.exception

open class DomainException(
    val errorCode: ErrorCode,
    val data: Any? = null,
) : RuntimeException(errorCode.message)
