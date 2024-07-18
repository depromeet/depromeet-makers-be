package com.depromeet.makers.infrastructure.db.entity

import com.depromeet.makers.domain.model.Attendance
import com.depromeet.makers.domain.model.AttendanceStatus
import com.depromeet.makers.domain.model.SessionType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity(name = "attendances")
class AttendanceEntity private constructor(
    @Id
    @Column(name = "id", length = 26, columnDefinition = "CHAR(26)", nullable = false)
    val id: String,

    @Column(name = "generation", nullable = false)
    val generation: Int,

    @Column(name = "week", nullable = false)
    val week: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: MemberEntity,

    @Enumerated(EnumType.STRING)
    @Column(name = "session_type", nullable = false, columnDefinition = "VARCHAR(255)")
    val sessionType: SessionType,

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status", nullable = false, columnDefinition = "VARCHAR(255)")
    val attendanceStatus: AttendanceStatus,

    @Column(name = "attendance_time", nullable = true)
    val attendanceTime: LocalDateTime?,

    @Column(name = "try_count", nullable = true)
    var tryCount: Int? = 0,
) {
    fun toDomain() = Attendance(
        attendanceId = id,
        generation = generation,
        week = week,
        member = member.toDomain(),
        attendanceStatus = attendanceStatus,
        sessionType = sessionType,
        attendanceTime = attendanceTime,
        tryCount = tryCount ?: 0
    )

    companion object {
        fun fromDomain(attendance: Attendance) = with(attendance) {
            AttendanceEntity(
                id = attendanceId,
                generation = generation,
                week = week,
                member = MemberEntity.fromDomain(member),
                sessionType = sessionType,
                attendanceStatus = attendanceStatus,
                attendanceTime = attendanceTime,
                tryCount = tryCount,
            )
        }
    }
}
