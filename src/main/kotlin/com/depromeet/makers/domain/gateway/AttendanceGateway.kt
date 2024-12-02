package com.depromeet.makers.domain.gateway

import com.depromeet.makers.domain.model.attendence.Attendance

interface AttendanceGateway {
    fun save(attendance: Attendance): Attendance

    fun getById(attendanceId: String): Attendance

    fun findByMemberIdAndGenerationAndWeek(memberId: String, generation: Int, week: Int): Attendance

    fun findAllByMemberIdAndGeneration(memberId: String, generation: Int): List<Attendance>

    fun findAllByGenerationAndWeek(generation: Int, week: Int): List<Attendance>
}
