package com.depromeet.makers.infrastructure.db.entity

import com.depromeet.makers.domain.model.Place
import com.depromeet.makers.domain.model.Session
import com.depromeet.makers.domain.model.SessionType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
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
    val description: String?,

    @Column(name = "start_time", nullable = false)
    val startTime: LocalDateTime,

    @Enumerated(EnumType.STRING)
    @Column(name = "session_type", nullable = false, columnDefinition = "VARCHAR(255)")
    val sessionType: SessionType,

    @Column(name = "address", nullable = true)
    val address: String?,

    @Column(name = "longitude", nullable = true)
    val longitude: Double?,

    @Column(name = "latitude", nullable = true)
    val latitude: Double?,

    @Column(name = "place_name", nullable = true, columnDefinition = "VARCHAR(255)")
    val placeName: String?,

    @Column(name = "place_id", nullable = true, columnDefinition = "VARCHAR(255)")
    val placeId: String?,

    @Column(name = "code", nullable = false)
    val code: String,
) {
    fun toDomain(): Session {
        val place = if (sessionType.isOffline()) {
            Place(
                placeId = placeId!!,
                name = placeName!!,
                address = address!!,
                longitude = longitude!!,
                latitude = latitude!!,
            )
        } else {
            null
        }

        return Session(
            sessionId = id,
            generation = generation,
            week = week,
            title = title,
            description = description,
            startTime = startTime,
            sessionType = sessionType,
            place = place,
            code = code,
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
                    address = place?.address,
                    longitude = place?.longitude,
                    latitude = place?.latitude,
                    placeName = place?.name,
                    placeId = place?.placeId,
                    code = code,
                )
            }
        }
    }
}
