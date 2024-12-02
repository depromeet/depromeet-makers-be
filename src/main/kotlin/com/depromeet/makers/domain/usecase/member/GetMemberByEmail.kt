package com.depromeet.makers.domain.usecase.member

import com.depromeet.makers.domain.exception.MemberNotFoundException
import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.model.member.Member
import com.depromeet.makers.domain.usecase.UseCase

class GetMemberByEmail(
    private val memberGateway: MemberGateway,
) : UseCase<GetMemberByEmail.GetMemberByEmailInput, Member> {
    data class GetMemberByEmailInput(
        val email: String,
    )

    override fun execute(input: GetMemberByEmailInput): Member {
        return memberGateway
            .findByEmail(email = input.email)
            ?: throw MemberNotFoundException()
    }
}
