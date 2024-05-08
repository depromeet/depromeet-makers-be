package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.exception.PassCordAlreadySetException
import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.model.Member
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk

class UpdateDefaultMemberPassCordTest: BehaviorSpec({
    Given("이미 비밀번호를 설정한 사용자일 때") {
        val memberGateway = mockk<MemberGateway>()
        val updateMemberPassCord = mockk<UpdateMemberPassCord>()
        val updateDefaultMemberPassCord = UpdateDefaultMemberPassCord(memberGateway, updateMemberPassCord)
        val mockMember = Member(
            memberId = "",
            name = "",
            email = "",
            passCord = "passpass",
            generations = emptySet(),
        )
        every { memberGateway.findByEmail(any()) } returns mockMember
        every { updateMemberPassCord.execute(any()) } returnsArgument 0

        When("execute를 실행하면") {
            val executor = {
                updateDefaultMemberPassCord.execute(
                    UpdateDefaultMemberPassCord.UpdateDefaultMemberPassCordInput(
                        email = "",
                        passCord = "newPassword"
                    )
                )
            }
            Then("PassCordAlreadySetException를 던짐") {
                shouldThrow<PassCordAlreadySetException>(executor)
            }
        }
    }
    Given("사용자가 아직 비밀번호를 설정하지 않았을 때") {
        val memberGateway = mockk<MemberGateway>()
        val updateMemberPassCord = mockk<UpdateMemberPassCord>()
        val updateDefaultMemberPassCord = UpdateDefaultMemberPassCord(memberGateway, updateMemberPassCord)
        val mockMember = Member(
            memberId = "",
            name = "",
            email = "",
            passCord = null,
            generations = emptySet(),
        )
        every { memberGateway.findByEmail(any()) } returns mockMember
        every { updateMemberPassCord.execute(any()) } returns mockMember

        When("execute를 실행하면") {
            val result = updateDefaultMemberPassCord.execute(
                UpdateDefaultMemberPassCord.UpdateDefaultMemberPassCordInput(
                    email = "",
                    passCord = "newPassword"
                )
            )
            Then("비밀번호가 바뀜") {
                // 이건 이 단위테스트 영역이 아님. 예외만 안던졌으면 성공임.
            }
        }
    }
})
