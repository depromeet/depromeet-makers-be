package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.SessionGateway
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import jakarta.persistence.EntityNotFoundException

class DeleteSessionTest : BehaviorSpec({
    Given("기존에 존재하는 세션을 삭제할 때") {
        val sessionGateway = mockk<SessionGateway>()
        val deleteSession = DeleteSession(sessionGateway)

        val mockSessionId = "123e4567-e89b-12d3-a456-426614174000"

        every { sessionGateway.delete(any()) } returns Unit

        When("execute가 실행되면") {
            deleteSession.execute(
                DeleteSession.DeleteSessionInput(
                    sessionId = mockSessionId,
                )
            )

            Then("세션은 삭제된다") {
            }
        }
    }

    Given("존재하지 않는 세션을 삭제할 때") {
        val sessionGateway = mockk<SessionGateway>()
        val deleteSession = DeleteSession(sessionGateway)

        val mockNotExistSessionId = "123e4567-e89b-12d3-a456-426614174000"

        every { sessionGateway.delete(any()) } throws EntityNotFoundException()

        When("execute가 실행되면") {
            val executor = {
                deleteSession.execute(
                    DeleteSession.DeleteSessionInput(
                        sessionId = mockNotExistSessionId,
                    )
                )
            }

            Then("EntityNotFoundException이 발생한다") {
                shouldThrow<EntityNotFoundException>(executor)
            }
        }

    }
})
