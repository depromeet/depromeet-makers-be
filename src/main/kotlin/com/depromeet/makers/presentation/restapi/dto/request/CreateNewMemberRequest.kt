package com.depromeet.makers.presentation.restapi.dto.request

import com.depromeet.makers.domain.model.MemberPosition
import com.depromeet.makers.domain.model.MemberRole
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Schema(description = "새로운 사용자 생성 요청")
data class CreateNewMemberRequest(
    @field:NotBlank
    @field:Size(max = 128)
    @Schema(description = "사용자 이름", example = "송영민")
    val name: String,

    @field:NotBlank
    @field:Email
    @Schema(description = "이메일", example = "yeongmin1061@gmail.com")
    val email: String,

    @field:Min(1) @field:Max(100)
    @Schema(description = "기수", example = "15")
    val generationId: Int,

    @NotNull
    @Schema(description = "역할", example = "ORGANIZER, MEMBER")
    val role: MemberRole,

    @NotNull
    @Schema(description = "포지션", example = "BACKEND, IOS, AOS, WEB, DESIGN, UNKNOWN")
    val position: MemberPosition,
)
