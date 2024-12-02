package com.depromeet.makers.domain.usecase.session

import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.session.Session
import com.depromeet.makers.domain.usecase.UseCase

class UpdateSessionPlace(
    private val sessionGateway: SessionGateway,
) : UseCase<UpdateSessionPlace.UpdateSessionInput, Session> {
    data class UpdateSessionInput(
        val sessionId: String,
        val address: String,
        val latitude: Double,
        val longitude: Double,
        val name: String?,
    )

    override fun execute(input: UpdateSessionInput): Session {
        val session = sessionGateway.getById(input.sessionId)
        val updatePlace = session.place.update(
            address = input.address,
            latitude = input.latitude,
            longitude = input.longitude,
            name = input.name,
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
