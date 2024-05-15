package com.depromeet.makers.domain.gateway

import com.depromeet.makers.domain.model.Attendance

interface AttendanceGateway {
    fun save(attendance: Attendance): Attendance

    fun findByMemberIdAndGenerationAndWeek(memberId: String, generation: Int, week: Int): Attendance

    fun findAllByMemberIdAndGeneration(memberId: String, generation: Int): List<Attendance>
}
