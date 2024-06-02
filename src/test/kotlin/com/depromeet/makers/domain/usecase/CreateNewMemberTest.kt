package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.exception.MemberAlreadyExistsException
import com.depromeet.makers.domain.gateway.AttendanceGateway
import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.Member
import com.depromeet.makers.domain.model.MemberGeneration
import com.depromeet.makers.domain.model.MemberPosition
import com.depromeet.makers.domain.model.MemberRole
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk


class CreateNewMemberTest : BehaviorSpec({
    Given("이미 존재하는 이메일의 사용자이지만 새로운 기수일 때") {
        val memberGateway = mockk<MemberGateway>()
        val sessionGateway = mockk<SessionGateway>()
        val attendanceGateway = mockk<AttendanceGateway>()
        val createNewMember = CreateNewMember(memberGateway, sessionGateway, attendanceGateway)

        val mockName = "송영민"
        val mockMemberId = "1"
        val mockEmail = "yeongmin1061@gmail.com"
        val mockGenerationId = 14
        val mockPreviousGenerationId = 12
        val mockPreviousGenerations = setOf(
            MemberGeneration(
                generationId = mockPreviousGenerationId,
                role = MemberRole.MEMBER,
                position = MemberPosition.BACKEND,
                groupId = null,
            )
        )

        every { memberGateway.findByEmail(any()) } answers {
            val email = firstArg<String>()
            Member(
                memberId = mockMemberId,
                name = mockName,
                email = email,
                passCord = null,
                generations = mockPreviousGenerations
            )
        }
        every { memberGateway.save(any()) } returnsArgument 0
        every { sessionGateway.findAllByGeneration(any()) } returns emptyList()
        every { attendanceGateway.save(any()) } returns mockk()


        When("execute가 실행하면") {
            val result = createNewMember.execute(
                CreateNewMember.CreateNewMemberInput(
                    name = mockName,
                    email = mockEmail,
                    generationId = mockGenerationId,
                    role = MemberRole.MEMBER,
                    position = MemberPosition.BACKEND,
                    groupId = null,
                )
            )
            Then("기수는 총 두개 (기존 1 + 신규 1)") {
                result.generations.size shouldBeExactly mockPreviousGenerations.size + 1
            }
            Then("새로운 기수가 존재") {
                result.generations.any { it.generationId == mockGenerationId } shouldBe true
            }
            Then("기존 기수도 존재") {
                result.generations.any { it.generationId == mockPreviousGenerationId } shouldBe true
            }
        }
    }

    Given("이미 존재하는 이메일의 사용자이고 이미 등록된 기수일 때") {
        val memberGateway = mockk<MemberGateway>()
        val sessionGateway = mockk<SessionGateway>()
        val attendanceGateway = mockk<AttendanceGateway>()
        val createNewMember = CreateNewMember(memberGateway, sessionGateway, attendanceGateway)

        val mockName = "송영민"
        val mockMemberId = "1"
        val mockEmail = "yeongmin1061@gmail.com"
        val mockGenerationId = 14
        val mockPreviousGenerationId = 14
        val mockPreviousGenerations = setOf(
            MemberGeneration(
                generationId = mockPreviousGenerationId,
                role = MemberRole.MEMBER,
                position = MemberPosition.BACKEND,
                groupId = null,
            )
        )

        every { memberGateway.findByEmail(any()) } answers {
            val email = firstArg<String>()
            Member(
                memberId = mockMemberId,
                name = mockName,
                email = email,
                passCord = null,
                generations = mockPreviousGenerations
            )
        }
        every { memberGateway.save(any()) } returnsArgument 0

        When("execute가 실행하면") {
            val executor = {
                createNewMember.execute(
                    CreateNewMember.CreateNewMemberInput(
                        name = mockName,
                        email = mockEmail,
                        generationId = mockGenerationId,
                        role = MemberRole.MEMBER,
                        position = MemberPosition.BACKEND,
                        groupId = null,
                    )
                )
            }
            Then("MemberAlreadyExistsException를 던짐") {
                shouldThrow<MemberAlreadyExistsException>(executor)
            }
        }
    }

    Given("처음 등록하는 사용자일 때") {
        val memberGateway = mockk<MemberGateway>()
        val sessionGateway = mockk<SessionGateway>()
        val attendanceGateway = mockk<AttendanceGateway>()
        val createNewMember = CreateNewMember(memberGateway, sessionGateway, attendanceGateway)

        val mockName = "송영민"
        val mockEmail = "yeongmin1061@gmail.com"
        val mockGenerationId = 14

        every { memberGateway.findByEmail(any()) } returns null
        every { memberGateway.save(any()) } returnsArgument 0
        every { sessionGateway.findAllByGeneration(any()) } returns emptyList()
        every { attendanceGateway.save(any()) } returns mockk()

        When("execute가 실행하면") {
            val result = createNewMember.execute(
                CreateNewMember.CreateNewMemberInput(
                    name = mockName,
                    email = mockEmail,
                    generationId = mockGenerationId,
                    role = MemberRole.MEMBER,
                    position = MemberPosition.BACKEND,
                    groupId = null,
                )
            )
            Then("입력한 정보가 전부 일치하고") {
                result.name shouldBeEqual mockName
                result.email shouldBeEqual mockEmail
            }
            Then("새로운 기수가 존재") {
                result.generations.any { it.generationId == mockGenerationId } shouldBe true
            }
        }
    }
})
