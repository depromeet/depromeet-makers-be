package com.depromeet.makers.domain.usecase.session

import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.usecase.UseCase

class DeleteSession(
    private val sessionGateway: SessionGateway
) : UseCase<DeleteSession.DeleteSessionInput, Unit> {
    data class DeleteSessionInput(
        val sessionId: String
    )

    override fun execute(input: DeleteSessionInput) {
        sessionGateway.delete(input.sessionId)
    }
}
