package com.depromeet.makers.presentation.restapi.controller

import com.depromeet.makers.domain.usecase.GetMemberById
import com.depromeet.makers.presentation.restapi.dto.response.MemberResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "나(Me) 관련 API", description = "로그인한 사용자의 정보를 관리하는 API")
@RestController
@RequestMapping("/v1/me")
class MeController(
    private val getMemberById: GetMemberById,
) {
    @PreAuthorize("hasAnyRole('MEMBER', 'ORGANIZER')")
    @Operation(summary = "로그인한 사용자 조회", description = "로그인한 사용자 정보를 조회합니다.")
    @GetMapping
    fun getMe(
        authentication: Authentication,
    ): MemberResponse {
        val member = getMemberById.execute(
            GetMemberById.GetMemberByIdInput(
                memberId = authentication.name,
            )
        )
        return MemberResponse.fromDomain(member)
    }
}
