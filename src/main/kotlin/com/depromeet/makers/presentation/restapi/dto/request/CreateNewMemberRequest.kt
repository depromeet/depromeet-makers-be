package com.depromeet.makers.presentation.restapi.dto.request

import com.depromeet.makers.domain.model.MemberPosition
import com.depromeet.makers.domain.model.MemberRole
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "새로운 사용자 생성 요청")
data class CreateNewMemberRequest(
    @NotBlank
    @Size(max = 128)
    @Schema(description = "사용자 이름", example = "송영민")
    val name: String,

    @NotBlank
    @Email
    @Schema(description = "이메일", example = "yeongmin1061@gmail.com")
    val email: String,

    @Min(1) @Max(100)
    @Schema(description = "기수", example = "15")
    val generationId: Int,

    @NotBlank
    @Schema(description = "역할", example = "ORGANIZER, MEMBER")
    val role: MemberRole,

    @NotBlank
    @Schema(description = "포지션", example = "BACKEND, IOS, AOS, WEB, DESIGN, UNKNOWN")
    val position: MemberPosition,
)
