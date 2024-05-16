package com.depromeet.makers.presentation.restapi.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "사용자 존재 유무")
data class CheckMemberExistsByEmailResponse(
    @Schema(description = "존재 유무", example = "true | false")
    val result: Boolean,
)
