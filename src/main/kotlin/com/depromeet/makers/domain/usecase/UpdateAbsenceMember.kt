package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.AttendanceGateway
import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.AttendanceStatus
import com.depromeet.makers.util.logger
import java.time.LocalDateTime

class UpdateAbsenceMember(
    private val sessionGateway: SessionGateway,
    private val attendanceGateway: AttendanceGateway,
) : UseCase<UpdateAbsenceMember.UpdateAbsenceMemberInput, Unit> {
    data class UpdateAbsenceMemberInput(
        val today: LocalDateTime,
    )

    override fun execute(input: UpdateAbsenceMemberInput) {
        val logger = logger()

        val session = sessionGateway.findByStartTimeBetween(input.today, input.today.plusDays(1))
        if (session == null) {
            return
        }

        val attendances = attendanceGateway.findAllByGenerationAndWeek(session.generation, session.week)
        attendances
            .filter {
                it.attendanceStatus == AttendanceStatus.ATTENDANCE_ON_HOLD
            }
            .forEach {
                attendanceGateway.save(it.copy(attendanceStatus = AttendanceStatus.ABSENCE))
                logger.info("memberId: ${it.member.memberId} has been changed to ${AttendanceStatus.ATTENDANCE}")
            }
    }
}
