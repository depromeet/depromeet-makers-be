package com.depromeet.makers.repository.client

import com.depromeet.makers.presentation.web.dto.response.KakaoAccountResponse
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.service.annotation.PostExchange

interface KakaoAuthClient {
    @PostExchange(url = "https://kapi.kakao.com/v2/user/me", contentType = "application/x-www-form-urlencoded;charset=utf-8")
    fun getKakaoAccount(@RequestHeader headers: Map<String, String>): KakaoAccountResponse
}
