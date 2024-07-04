package com.depromeet.makers.infrastructure.db.entity

import com.depromeet.makers.domain.model.Place
import com.depromeet.makers.domain.model.Session
import com.depromeet.makers.domain.model.SessionType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "session")
class SessionEntity private constructor(
    @Id
    @Column(name = "id", length = 26, columnDefinition = "CHAR(26)", nullable = false)
    val id: String,

    @Column(name = "generation", nullable = false)
    val generation: Int,

    @Column(name = "week", nullable = false)
    val week: Int,

    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "description", nullable = false)
    var description: String?,

    @Column(name = "start_time", nullable = false)
    val startTime: LocalDateTime,

    @Enumerated(EnumType.STRING)
    @Column(name = "session_type", nullable = false, columnDefinition = "VARCHAR(255)")
    val sessionType: SessionType,

    @Column(name = "address")
    var address: String,

    @Column(name = "longitude")
    var longitude: Double,

    @Column(name = "latitude")
    var latitude: Double,

    @Column(name = "place_name", nullable = true, columnDefinition = "VARCHAR(255)")
    var placeName: String?,

    ) {
    fun toDomain(): Session {
        return Session(
            sessionId = id,
            generation = generation,
            week = week,
            title = title,
            description = description,
            startTime = startTime,
            sessionType = sessionType,
            place = Place.newPlace(address, longitude, latitude, placeName),
        )
    }

    companion object {
        fun fromDomain(session: Session): SessionEntity {
            return with(session) {
                SessionEntity(
                    id = sessionId,
                    generation = generation,
                    week = week,
                    title = title,
                    description = description,
                    startTime = startTime,
                    sessionType = sessionType,
                    address = place.address,
                    longitude = place.longitude,
                    latitude = place.latitude,
                    placeName = place.name,
                )
            }
        }
    }
}
