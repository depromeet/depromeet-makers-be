package com.depromeet.makers.presentation.web.dto.response

import com.depromeet.makers.domain.vo.TokenPair

data class AuthenticationResponse(
    val accessToken: String,
    val refreshToken: String,
) {
    companion object {
        fun from(tokenPair: TokenPair) = AuthenticationResponse(
            accessToken = tokenPair.accessToken,
            refreshToken = tokenPair.refreshToken,
        )
    }
}
