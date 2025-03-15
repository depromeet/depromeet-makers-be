package com.depromeet.makers.repository.client

import com.depromeet.makers.presentation.web.dto.response.AppleKeyListResponse
import org.springframework.web.service.annotation.GetExchange

interface AppleAuthClient {
    @GetExchange("https://appleid.apple.com/auth/keys")
    fun getApplyKeys(): AppleKeyListResponse
}
