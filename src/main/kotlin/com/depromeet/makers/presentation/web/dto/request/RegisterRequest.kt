package com.depromeet.makers.presentation.web.dto.request

import com.depromeet.makers.domain.enums.MemberPosition

data class RegisterRequest(
    val name: String,
    val email: String,
    val position: MemberPosition,

    val appleLogin: AppleLoginRequest?,
    val kakaoLogin: KakaoLoginRequest?,
) {
    init {
        require(appleLogin != null || kakaoLogin != null) { "appleLogin or kakaoLogin must be provided" }
    }
}
