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
    @Column(name = "session_type", nullable = false)
    val sessionType: SessionType,

    @Column(name = "address")
    var address: String,

    @Column(name = "x")
    var x: Double,

    @Column(name = "y")
    var y: Double,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "member_id")
    var attendanceMembers: Set<SessionAttendanceEntity>,
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
            place = Place.newPlace(address, x, y),
            attendanceMemberIds = attendanceMembers.map { it.toDomain() }.toSet(),
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
                    x = place.x,
                    y = place.y,
                    attendanceMembers = emptySet(),
                )
            }
        }
    }
}
