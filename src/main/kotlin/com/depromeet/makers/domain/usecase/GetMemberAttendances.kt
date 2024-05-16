package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.AttendanceGateway
import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.Attendance
import com.depromeet.makers.domain.model.Session

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

        // 기본값 없으면 세팅 (추후 걷어낼 로직)
        val sessions = (1..16).map {
            sessionGateway.findByGenerationAndWeek(input.generation, it) ?: sessionGateway.save(
                Session.newSession(
                    generation = input.generation,
                    week = it,
                )
            )
        }

        val attendances = (1..16).map {
            attendanceGateway.save(
                Attendance.newAttendance(
                    member = member,
                    generation = input.generation,
                    week = it,
                    sessionType = sessions[it - 1].sessionType,
                )
            )
        }

        var offlineAbsenceScore = 0.0
        var totalAbsenceScore = 0.0
        (1..16).forEach { week ->
            offlineAbsenceScore += if (attendances[week - 1].attendanceStatus.isAbsence() && sessions[week - 1].isOffline()) 1.0 else 0.0
            totalAbsenceScore += attendances[week - 1].attendanceStatus.point
        }
        return GetMemberAttendancesOutput(
            generation = input.generation,
            offlineAbsenceScore = offlineAbsenceScore,
            totalAbsenceScore = totalAbsenceScore,
            attendances = attendances.sortedBy { it.week }
        )
    }
}
