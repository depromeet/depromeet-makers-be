package com.depromeet.makers.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("depromeet")
data class DepromeetProperties(
    val generation: Int,
)
