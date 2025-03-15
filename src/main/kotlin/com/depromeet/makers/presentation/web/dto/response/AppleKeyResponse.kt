package com.depromeet.makers.presentation.web.dto.response

data class AppleKeyResponse(
    val kty: String,
    val kid: String,
    val use: String,
    val alg: String,
    val n: String,
    val e: String,
)

data class AppleKeyListResponse(
    val keys: List<AppleKeyResponse>,
)
