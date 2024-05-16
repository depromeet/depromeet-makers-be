package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.AttendanceGateway
import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.Attendance

class GetMemberAttendances(
    private val attendanceGateway: AttendanceGateway,
    private val memberGateway: MemberGateway,
    private val sessionGateway: SessionGateway,
) : UseCase<GetMemberAttendances.GetMemberAttendancesInput, GetMemberAttendances.GetMemberAttendancesOutput> {
    data class GetMemberAttendancesInput(
        val memberId: String,
        val generation: Int,
    )

    data class GetMemberAttendancesOutput(
        val generation: Int,
        val offlineAbsenceScore: Double,
        val totalAbsenceScore: Double,
        val attendances: List<Attendance>,
    )

    override fun execute(input: GetMemberAttendancesInput): GetMemberAttendancesOutput {
        val member = memberGateway.getById(input.memberId)

        val attendances = attendanceGateway.findAllByMemberIdAndGeneration(member.memberId, input.generation)
            .associate { it.week to it }
            .toMutableMap()

        (1..16).filter { week -> !attendances.contains(week) }
            .forEach { week ->
                attendances[week] = attendanceGateway.save(
                    Attendance.newAttendance(
                        member = member,
                        generation = input.generation,
                        week = week,
                    )
                )
            }

        var offlineAbsenceScore = 0.0
        var totalAbsenceScore = 0.0
        attendances.values.forEach {
            offlineAbsenceScore += if (it.attendanceStatus.isAbsence() &&
                runCatching { sessionGateway.findByGenerationAndWeek(input.generation, it.week).isOffline() }
                    .getOrDefault(false)
            ) 1.0 else 0.0
            totalAbsenceScore += it.attendanceStatus.point
        }
        return GetMemberAttendancesOutput(
            generation = input.generation,
            offlineAbsenceScore = offlineAbsenceScore,
            totalAbsenceScore = totalAbsenceScore,
            attendances = attendances.values.sortedBy { it.week }
        )
    }
}
