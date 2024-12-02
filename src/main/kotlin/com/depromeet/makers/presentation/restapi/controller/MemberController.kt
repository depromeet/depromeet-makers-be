package com.depromeet.makers.presentation.restapi.controller

import com.depromeet.makers.domain.usecase.member.CreateNewMember
import com.depromeet.makers.presentation.restapi.dto.member.request.CreateNewMemberRequest
import com.depromeet.makers.presentation.restapi.dto.member.response.CreateNewMemberResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "사용자 관련 API", description = "사용자의 정보를 관리하는 API")
@RestController
@RequestMapping("/v1/members")
class MemberController(
    private val createNewMember: CreateNewMember,
) {
    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "새로운 사용자 생성", description = "새로운 사용자를 생성합니다.")
    @PostMapping
    fun createNewMember(
        @RequestBody @Valid request: CreateNewMemberRequest,
    ): CreateNewMemberResponse {
        val member = createNewMember.execute(
            CreateNewMember.CreateNewMemberInput(
                name = request.name,
                email = request.email,
                generationId = request.generationId,
                role = request.role,
                position = request.position,
                groupId = request.groupId,
            )
        )
        return CreateNewMemberResponse.fromDomain(member)
    }
}
