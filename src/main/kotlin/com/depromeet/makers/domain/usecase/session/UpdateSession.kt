package com.depromeet.makers.domain.usecase.session

import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.session.Session
import com.depromeet.makers.domain.model.session.SessionType
import com.depromeet.makers.domain.usecase.UseCase
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
