package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.AttendanceGateway
import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.model.Attendance

class GetMemberAttendances(
    private val attendanceGateway: AttendanceGateway,
    private val memberGateway: MemberGateway,
) : UseCase<GetMemberAttendances.GetMemberAttendancesInput, List<Attendance>> {
    data class GetMemberAttendancesInput(
        val memberId: String,
        val generation: Int,
    )

    override fun execute(input: GetMemberAttendancesInput): List<Attendance> {
        val member = memberGateway.getById(input.memberId)

        val attendances = attendanceGateway.findAllByMemberIdAndGeneration(member.memberId, input.generation)
            .associate { it.week to it }
            .toMutableMap()

        (1..16).filter { week -> !attendances.contains(week) }
            .forEach { week ->
                attendances[week] = Attendance.newAttendance(
                    member = member,
                    generation = input.generation,
                    week = week,
                )
            }

        return attendances.values.sortedBy { it.week }
    }
}
