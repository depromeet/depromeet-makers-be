package com.depromeet.makers.infrastructure.gateway

import com.depromeet.makers.domain.gateway.AttendanceGateway
import com.depromeet.makers.domain.model.Attendance
import com.depromeet.makers.infrastructure.db.entity.AttendanceEntity
import com.depromeet.makers.infrastructure.db.repository.JpaAttendanceRepository
import org.springframework.stereotype.Component

@Component
class AttendanceGatewayImpl(
    private val jpaAttendanceRepository: JpaAttendanceRepository,
) : AttendanceGateway {

    override fun save(attendance: Attendance): Attendance {
        val attendanceEntity = AttendanceEntity.fromDomain(attendance)
        return jpaAttendanceRepository
            .save(attendanceEntity)
            .toDomain()
    }

    override fun findByMemberIdAndGenerationAndWeek(
        memberId: String,
        generation: Int,
        week: Int
    ): Attendance {
        return jpaAttendanceRepository
            .findByMemberIdAndGenerationAndWeek(memberId, generation, week)
            ?.toDomain()
            ?: throw NoSuchElementException("해당하는 회원의 세대, 주차 출석 정보가 없습니다.")
    }

    override fun findAllByMemberIdAndGeneration(memberId: String, generation: Int): List<Attendance> {
        return jpaAttendanceRepository
            .findAllByMemberIdAndGeneration(memberId, generation)
            .map { it.toDomain() }
    }

    override fun getById(attendanceId: String): Attendance {
        return jpaAttendanceRepository
            .findById(attendanceId)
            .orElseThrow { NoSuchElementException("해당하는 출석 정보가 없습니다.") }
            .toDomain()
    }
}
