package com.depromeet.makers.domain.exception

class DomainException(
    val errorCode: ErrorCode,
) : RuntimeException()
