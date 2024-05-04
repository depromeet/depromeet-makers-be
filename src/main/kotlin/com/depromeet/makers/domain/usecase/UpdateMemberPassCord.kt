package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.model.Member
import com.depromeet.makers.util.EncryptUtils

class UpdateMemberPassCord(
    private val memberGateway: MemberGateway,
) : UseCase<UpdateMemberPassCord.UpdateMemberPassCordInput, Member> {
    data class UpdateMemberPassCordInput(
        val memberId: String,
        val passCord: String,
    )

    override fun execute(input: UpdateMemberPassCordInput): Member {
        val member = memberGateway.getById(input.memberId)
        val encryptedPassCord = EncryptUtils.encrypt(input.passCord)
        return memberGateway.save(
            member.initializePassCord(passCord = encryptedPassCord)
        )
    }
}
