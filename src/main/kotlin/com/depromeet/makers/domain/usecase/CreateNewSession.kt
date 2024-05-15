package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.exception.SessionAlreadyExistsException
import com.depromeet.makers.domain.exception.SessionPlaceNotFoundException
import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.Place
import com.depromeet.makers.domain.model.Session
import com.depromeet.makers.domain.model.SessionType
import com.depromeet.makers.util.generateULID
import java.time.LocalDateTime

class CreateNewSession(
    private val sessionGateWay: SessionGateway,
) : UseCase<CreateNewSession.CreateNewSessionInput, Session> {
    data class CreateNewSessionInput(
        val generation: Int,
        val week: Int,
        val title: String,
        val description: String?,
        val startTime: LocalDateTime,
        val sessionType: SessionType,
        val address: String?,
        val longitude: Double?,
        val latitude: Double?,
    )

    override fun execute(input: CreateNewSessionInput): Session {
        if (hasSameGenerationAndWeekSession(input.generation, input.week)) {
            throw SessionAlreadyExistsException()
        }

        val newSession = Session(
            sessionId = generateULID(),
            generation = input.generation,
            week = input.week,
            title = input.title,
            description = input.description,
            startTime = input.startTime,
            sessionType = input.sessionType,
            place = getNewPlace(input),
        )
        return sessionGateWay.save(newSession)
    }

    private fun hasSameGenerationAndWeekSession(generation: Int, week: Int) =
        sessionGateWay.existsByGenerationAndWeek(generation, week)

    private fun getNewPlace(input: CreateNewSessionInput) =
        if (input.sessionType.isOnline()) {
            Place.emptyPlace()
        } else {
            if (input.address == null || input.longitude == null || input.latitude == null) {
                throw SessionPlaceNotFoundException()
            }
            Place.newPlace(
                address = input.address,
                longitude = input.longitude,
                latitude = input.latitude,
            )
        }
}
