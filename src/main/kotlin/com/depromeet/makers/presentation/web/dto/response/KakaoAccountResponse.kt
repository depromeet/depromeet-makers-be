package com.depromeet.makers.presentation.web.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoAccountResponse(
    @JsonProperty("id")
    val id: Long,

    @JsonProperty("connected_at")
    val connectedAt: String,

    @JsonProperty("kakao_account")
    val kakaoAccount: KakaoAccount,
)

data class KakaoAccount(
    @JsonProperty("profile_nickname_needs_agreement")
    val profileNicknameNeedsAgreement: Boolean,

    @JsonProperty("name_needs_agreement")
    val nameNeedsAgreement: Boolean,

    @JsonProperty("has_email")
    val hasEmail: Boolean,

    @JsonProperty("email")
    val email: String?,

    @JsonProperty("email_needs_agreement")
    val emailNeedsAgreement: Boolean,

    @JsonProperty("has_phone_number")
    val hasPhoneNumber: Boolean,

    @JsonProperty("phone_number_needs_agreement")
    val phoneNumberNeedsAgreement: Boolean,

    @JsonProperty("phone_number")
    val phoneNumber: String?,
)
