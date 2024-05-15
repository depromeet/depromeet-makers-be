package com.depromeet.makers.infrastructure.db.repository

import com.depromeet.makers.infrastructure.db.entity.AttendanceEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaAttendanceRepository : JpaRepository<AttendanceEntity, String> {

    fun save(attendance: AttendanceEntity): AttendanceEntity

    fun findAllByGenerationAndWeek(generation: Int, week: Int): List<AttendanceEntity>

    fun findByMemberIdAndGenerationAndWeek(memberId: String, generation: Int, week: Int): AttendanceEntity?
}
