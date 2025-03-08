package com.depromeet.makers.domain.exception

import java.lang.RuntimeException

class DomainException(
    val errorCode: ErrorCode,
): RuntimeException() {
}
