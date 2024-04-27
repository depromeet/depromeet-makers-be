package com.depromeet.makers.presentation.restapi.controller

import com.depromeet.makers.domain.use_case.CreateNewMember
import com.depromeet.makers.presentation.restapi.dto.request.CreateNewMemberRequest
import com.depromeet.makers.presentation.restapi.dto.response.CreateNewMemberResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/members")
class MemberController(
    private val createNewMember: CreateNewMember,
) {
    @PostMapping
    fun createNewMember(
        @RequestBody request: CreateNewMemberRequest,
    ): CreateNewMemberResponse {
        val member = createNewMember.execute(
            CreateNewMember.CreateNewMemberInput(
                name = request.name
            )
        )
        return CreateNewMemberResponse.fromDomain(member)
    }
}
