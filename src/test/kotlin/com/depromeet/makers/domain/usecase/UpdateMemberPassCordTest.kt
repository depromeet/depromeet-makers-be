package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.model.Member
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class UpdateMemberPassCordTest: BehaviorSpec({
    Given("이미 존재하는 사용자일 때") {
        val memberGateway = mockk<MemberGateway>()
        val updateMemberPassCord = UpdateMemberPassCord(memberGateway)

        val testMemberId = "test"
        val testPassCord = "very-secret-password"
        val mockMember = Member(
            memberId = testMemberId,
            name = "",
            email = "",
            passCord = null,
            generations = emptySet(),
        )
        every { memberGateway.getById(any()) } returns mockMember
        every { memberGateway.save(any()) } returnsArgument 0

        When("execute를 실행하면") {
            val result = updateMemberPassCord.execute(
                UpdateMemberPassCord.UpdateMemberPassCordInput(
                    memberId = testMemberId,
                    passCord = testPassCord,
                )
            )
            Then("비밀번호가 변경되었다") {
                result.passCord shouldBe testPassCord
            }
        }
    }
})
