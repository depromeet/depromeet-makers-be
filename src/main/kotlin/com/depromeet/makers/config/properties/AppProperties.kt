package com.depromeet.makers.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
data class AppProperties(
    val token: TokenProperties,
    val depromeet: DepromeetProperties,
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

    data class DepromeetProperties(
        val generation: Int,
    )
}

