package com.depromeet.makers.domain.exception

enum class ErrorCode(
    val code: Int,
    val message: String,
) {
    TEST_NOT_FOUND(4000, "테스트를 찾을 수 없어요");

    fun isCriticalError(): Boolean {
        return this.code >= 5000
    }
}
