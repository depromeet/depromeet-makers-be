package com.depromeet.makers.presentation.restapi.dto.response

import com.depromeet.makers.domain.model.Member
import com.depromeet.makers.domain.model.MemberPosition
import com.depromeet.makers.domain.model.MemberRole
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "사용자 생성 결과 DTO")
data class CreateNewMemberResponse(
    @Schema(description = "사용자 ID", example = "01HWPNRE5TS9S7VC99WPETE5KE")
    val id: String,

    @Schema(description = "사용자 이름", example = "송영민")
    val name: String,

    @Schema(description = "사용자 이메일", example = "yeongmin1061@gmail.com")
    val email: String,

    @Schema(description = "기수 정보")
    val generations: List<CreateNewMemberGenerationResponse>,
) {
    @Schema(description = "사용자 기수 정보")
    data class CreateNewMemberGenerationResponse(
        @Schema(description = "기수", example = "15")
        val generationId: Int,

        @Schema(description = "역할", example = "ORGANIZER")
        val role: MemberRole,

        @Schema(description = "포지션", example = "BACKEND")
        val position: MemberPosition,
    )

    companion object {
        fun fromDomain(member: Member) = with(member) {
            CreateNewMemberResponse(
                id = memberId,
                name = name,
                email = email,
                generations = member.generations.map {
                    CreateNewMemberGenerationResponse(
                        generationId = it.generationId,
                        role = it.role,
                        position = it.position
                    )
                }
            )
        }
    }
}
