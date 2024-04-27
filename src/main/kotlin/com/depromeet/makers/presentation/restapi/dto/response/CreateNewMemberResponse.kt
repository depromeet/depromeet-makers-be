package com.depromeet.makers.presentation.restapi.dto.response

import com.depromeet.makers.domain.model.Member
import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class CreateNewMemberResponse(
    val id: String,
    val name: String,
) {
    companion object {
        fun fromDomain(member: Member) = with(member) {
            CreateNewMemberResponse(
                id = memberId,
                name = name,
            )
        }
    }
}
