package com.depromeet.makers.presentation.restapi.dto.member.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "사용자 존재 유무")
data class CheckMemberExistsByEmailResponse(
    @Schema(description = "사용자 존재 유무", example = "true | false")
    val isMemberExists: Boolean,

    @Schema(description = "패스워드 발급 유무", example = "true | false")
    val isPassCordAssigned: Boolean,
)
