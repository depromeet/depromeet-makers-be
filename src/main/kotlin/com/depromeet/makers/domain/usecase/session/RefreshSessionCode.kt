package com.depromeet.makers.domain.usecase.session

import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.session.Session
import com.depromeet.makers.domain.usecase.UseCase

class RefreshSessionCode(
    val sessionGateway: SessionGateway,
) : UseCase<String, Session> {
    override fun execute(sessionId: String): Session {
        val session = sessionGateway.getById(sessionId)

        return sessionGateway.save(
            session.update(
                code = Session.generateCode(),
            ),
        )
    }
}
