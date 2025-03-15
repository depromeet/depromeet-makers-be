package com.depromeet.makers.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
data class AppProperties(
    val token: TokenProperties
) {
    data class TokenProperties(
        val secretKey: String,
        val expiration: Expiration
    ) {
        data class Expiration(
            val access: Long,
            val refresh: Long,
        )
    }
}

