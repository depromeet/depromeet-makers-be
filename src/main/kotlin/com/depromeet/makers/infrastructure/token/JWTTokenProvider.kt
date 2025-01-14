package com.depromeet.makers.infrastructure.token

import com.depromeet.makers.domain.exception.AuthenticationTokenExpiredException
import com.depromeet.makers.domain.exception.AuthenticationTokenGenerationExpiredException
import com.depromeet.makers.domain.exception.AuthenticationTokenNotValidException
import com.depromeet.makers.properties.DepromeetProperties
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class JWTTokenProvider(
    @Value("\${app.token.secretKey}") private val verifyKey: String,
    @Value("\${app.token.expiration.access}") private val accessTokenExpiration: Long,
    @Value("\${app.token.expiration.refresh}") private val refreshTokenExpiration: Long,
    private val depromeetProperties: DepromeetProperties,
) {
    private final val signKey: SecretKey = SecretKeySpec(verifyKey.toByteArray(), "AES")
    private val jwtParser = Jwts
        .parser()
        .decryptWith(signKey)
        .build()

    fun generateAccessToken(authentication: Authentication): String {
        val authorities = authentication.authorities.joinToString(",") {
            it.authority
        }
        return Jwts.builder()
            .header()
            .add(TOKEN_TYPE_HEADER_KEY, ACCESS_TOKEN_TYPE_VALUE)
            .and()
            .claims()
            .add(USER_ID_CLAIM_KEY, authentication.name)
            .add(AUTHORITIES_CLAIM_KEY, authorities)
            .add(GENERATION_KEY, depromeetProperties.generation)
            .and()
            .expiration(generateAccessTokenExpiration())
            .encryptWith(signKey, Jwts.ENC.A128CBC_HS256)
            .compact()
    }

    fun generateRefreshToken(authentication: Authentication): String {
        return Jwts.builder()
            .header()
            .add(TOKEN_TYPE_HEADER_KEY, REFRESH_TOKEN_TYPE_VALUE)
            .and()
            .claims()
            .add(USER_ID_CLAIM_KEY, authentication.name)
            .and()
            .expiration(generateRefreshTokenExpiration())
            .encryptWith(signKey, Jwts.ENC.A128CBC_HS256)
            .compact()
    }

    fun parseAuthentication(accessToken: String): Authentication {
        val claims = runCatching {
            jwtParser.parseEncryptedClaims(accessToken)
        }.getOrElse {
            when (it) {
                is ExpiredJwtException -> throw AuthenticationTokenExpiredException()
                else -> throw AuthenticationTokenNotValidException()
            }
        }
        val tokenType = claims.header[TOKEN_TYPE_HEADER_KEY] ?: throw RuntimeException()
        if (tokenType != ACCESS_TOKEN_TYPE_VALUE) throw RuntimeException()

        val generation = claims.payload[GENERATION_KEY] ?: throw AuthenticationTokenGenerationExpiredException()
        if (generation != depromeetProperties.generation) throw AuthenticationTokenGenerationExpiredException()

        val userId = claims.payload[USER_ID_CLAIM_KEY] as? String? ?: throw RuntimeException()
        val authorities = claims.payload[AUTHORITIES_CLAIM_KEY]?.toString()
            ?.split(",")
            ?.map { SimpleGrantedAuthority(it) }
            ?: emptyList()

        return UsernamePasswordAuthenticationToken(
            userId,
            accessToken,
            authorities,
        )
    }

    fun getMemberIdFromRefreshToken(refreshToken: String): String {
        val claims = jwtParser.parseEncryptedClaims(refreshToken)
        val tokenType = claims.header[TOKEN_TYPE_HEADER_KEY] ?: throw RuntimeException()
        if (tokenType != REFRESH_TOKEN_TYPE_VALUE) throw RuntimeException()

        return claims.payload[USER_ID_CLAIM_KEY] as? String ?: throw RuntimeException()
    }

    private fun generateAccessTokenExpiration() = Date(System.currentTimeMillis() + accessTokenExpiration * 1000)

    private fun generateRefreshTokenExpiration() = Date(System.currentTimeMillis() + refreshTokenExpiration * 1000)

    companion object {
        const val USER_ID_CLAIM_KEY = "user_id"
        const val AUTHORITIES_CLAIM_KEY = "authorities"
        const val GENERATION_KEY = "generation"

        const val TOKEN_TYPE_HEADER_KEY = "token_type"
        const val ACCESS_TOKEN_TYPE_VALUE = "access_token"
        const val REFRESH_TOKEN_TYPE_VALUE = "refresh_token"
    }
}
