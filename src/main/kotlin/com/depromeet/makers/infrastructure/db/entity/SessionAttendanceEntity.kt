package com.depromeet.makers.infrastructure.db.entity

import com.depromeet.makers.domain.model.AttendanceStatus
import com.depromeet.makers.domain.model.SessionAttendance
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import java.io.Serializable

@IdClass(SessionAttendanceEntityKey::class)
@Entity(name = "session_attendance")
class SessionAttendanceEntity private constructor(
    @Id
    @Column(name = "session_id", length = 26, columnDefinition = "CHAR(26)", nullable = false)
    val sessionId: String,

    @Id
    @Column(name = "member_id", length = 26, columnDefinition = "CHAR(26)", nullable = false)
    val memberId: String,

    @Column(name = "attendance", nullable = false)
    val attendance: AttendanceStatus,
) {
    fun toDomain() = SessionAttendance(
        attendance = attendance,
    )

    companion object {
        fun fromDomain(sessionId: String, memberId: String, sessionAttendance: SessionAttendance) = with(sessionAttendance) {
            SessionAttendanceEntity(
                sessionId = sessionId,
                memberId = memberId,
                attendance = attendance,
            )
        }
    }
}

class SessionAttendanceEntityKey(
    val sessionId: String,
    val memberId: String,
) : Serializable
