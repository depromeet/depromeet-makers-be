package com.depromeet.makers.components

import com.depromeet.makers.domain.exception.DomainException
import com.depromeet.makers.domain.exception.ErrorCode
import com.depromeet.makers.presentation.web.dto.response.AppleKeyListResponse
import com.depromeet.makers.presentation.web.dto.response.AppleKeyResponse
import com.depromeet.makers.presentation.web.dto.response.KakaoAccountResponse
import com.depromeet.makers.util.logger
import com.fasterxml.jackson.databind.ObjectMapper
import com.nimbusds.jose.jwk.JWK
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.util.Base64

@Component
class SocialLoginProvider(
    private val restClient: RestClient,
    private val objectMapper: ObjectMapper,
) {
    private val logger = logger()

    fun authenticateFromKakao(accessToken: String): KakaoAccountResponse = restClient
        .post()
        .uri("https://kapi.kakao.com/v2/user/me")
        .headers {
            it.setBearerAuth(accessToken)
            it.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
        }
        .retrieve()
        .onStatus({ it.isError }) { _, res ->
            val bodyValue = res.body.readAllBytes().toString(Charsets.UTF_8)
            logger.error("[SocialLoginProvider] failed kakaologin with response - $bodyValue")
            throw DomainException(ErrorCode.KAKAO_LOGIN_FAILED)
        }
        .body(KakaoAccountResponse::class.java)!!

    fun authenticateFromApple(identityToken: String): String {
        val pubKeys = getAppleKeys()
        return getUserIdFromAppleIdentity(pubKeys.keys.toTypedArray(), identityToken)
    }

    private fun getAppleKeys(): AppleKeyListResponse = restClient
        .get()
        .uri("https://appleid.apple.com/auth/keys")
        .retrieve()
        .onStatus({ it.isError }) { _, res ->
            val bodyValue = res.body.readAllBytes().toString(Charsets.UTF_8)
            logger.error("[SocialLoginProvider] failed applekey with response - $bodyValue")
            throw DomainException(ErrorCode.INTERNAL_ERROR)
        }
        .body(AppleKeyListResponse::class.java)!!

    private fun getUserIdFromAppleIdentity(keys: Array<AppleKeyResponse>, identityToken: String): String {
        val tokenParts = identityToken.split("\\.")
        val headerPart = String(Base64.getDecoder().decode(tokenParts[0]))
        val headerNode = objectMapper.readTree(headerPart)
        val kid = headerNode.get("kid").asText()

        val keyStr = keys.firstOrNull { it.kid == kid }
            ?: throw RuntimeException("Apple Key not found")

        val pubKey = JWK.parse(objectMapper.writeValueAsString(keyStr))
            .toRSAKey()
            .toRSAPublicKey()

        return Jwts.parser()
            .verifyWith(pubKey)
            .build()
            .parseSignedClaims(identityToken)
            .payload
            .get("sub", String::class.java)
    }
}
