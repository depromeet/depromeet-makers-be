package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.Session

class UpdateSessionPlace(
    private val sessionGateway: SessionGateway,
) : UseCase<UpdateSessionPlace.UpdateSessionInput, Session> {
    data class UpdateSessionInput(
        val sessionId: String,
        val address: String,
        val latitude: Double,
        val longitude: Double,
    )

    override fun execute(input: UpdateSessionInput): Session {
        val session = sessionGateway.getById(input.sessionId)
        val updatePlace = session.place.update(
            address = input.address,
            latitude = input.latitude,
            longitude = input.longitude,
        )

        return sessionGateway.save(
            session.update(
                generation = session.generation,
                week = session.week,
                title = session.title,
                description = session.description,
                startTime = session.startTime,
                sessionType = session.sessionType,
                place = updatePlace,
            )
        )
    }
}
