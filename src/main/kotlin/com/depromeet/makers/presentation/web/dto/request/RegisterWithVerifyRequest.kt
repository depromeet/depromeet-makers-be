package com.depromeet.makers.presentation.web.dto.request

data class RegisterWithVerifyRequest(
    val memberId: String,
    val code: String,

    val appleLogin: AppleLoginRequest?,
    val kakaoLogin: KakaoLoginRequest?,
) {
    init {
        require(appleLogin != null || kakaoLogin != null) { "appleLogin or kakaoLogin must be provided" }
    }
}
