package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.Session
import com.depromeet.makers.domain.model.SessionType
import java.time.LocalDateTime

class UpdateSession(
    private val sessionGateway: SessionGateway,
) : UseCase<UpdateSession.UpdateSessionInput, Session> {
    data class UpdateSessionInput(
        val sessionId: String,
        val title: String?,
        val description: String?,
        val startTime: LocalDateTime?,
        val sessionType: SessionType?,
    )

    override fun execute(input: UpdateSessionInput): Session {
        val session = sessionGateway.getById(input.sessionId)

        return sessionGateway.save(
            session.update(
                title = input.title ?: session.title,
                description = input.description,
                startTime = input.startTime ?: session.startTime,
                sessionType = input.sessionType ?: session.sessionType,
            )
        )
    }
}
