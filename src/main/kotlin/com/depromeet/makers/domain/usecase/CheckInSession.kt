package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.exception.InvalidCheckInDistanceException
import com.depromeet.makers.domain.exception.InvalidCheckInTimeException
import com.depromeet.makers.domain.exception.MissingPlaceParamException
import com.depromeet.makers.domain.gateway.AttendanceGateway
import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.Attendance
import java.time.DayOfWeek
import java.time.LocalDateTime
import kotlin.math.*

class CheckInSession(
    private val attendanceGateway: AttendanceGateway,
    private val sessionGateway: SessionGateway,
    private val memberGateway: MemberGateway,
) : UseCase<CheckInSession.CheckInSessionInput, Unit> {
    data class CheckInSessionInput(
        val now: LocalDateTime,
        val memberId: String,
        val longitude: Double?,
        val latitude: Double?,
    )

    override fun execute(input: CheckInSessionInput) {
        val member = memberGateway.getById(input.memberId)
        val monday = input.now.getMonday()

        val thisWeekSession = sessionGateway.findByStartTimeBetween(
            monday,
            monday.plusDays(7)
        ) ?: throw InvalidCheckInTimeException()

        val attendance = runCatching {
            attendanceGateway.findByMemberIdAndGenerationAndWeek(
                member.memberId,
                thisWeekSession.generation,
                thisWeekSession.week
            )
        }.getOrDefault(
            Attendance.newAttendance(
                generation = thisWeekSession.generation,
                week = thisWeekSession.week,
                member = member,
            )
        )

        // 오프라인 세션의 경우 거리 확인 로직
        if (thisWeekSession.isOffline()) {
            if (input.latitude == null || input.longitude == null) {
                throw MissingPlaceParamException()
            }

            val distance = calculateDistance(
                thisWeekSession.place.latitude,
                thisWeekSession.place.longitude,
                input.latitude,
                input.longitude,
            )

            if (!inRange(distance, 100.0)) { // 100m 기준 (임시 설정)
                throw InvalidCheckInDistanceException()
            }
        }

        attendanceGateway.save(attendance.checkIn(input.now, thisWeekSession.startTime))
    }

    private fun LocalDateTime.getMonday(): LocalDateTime {
        val dayOfWeek = this.dayOfWeek
        val daysToAdd = DayOfWeek.MONDAY.value - dayOfWeek.value
        return this.plusDays(daysToAdd.toLong())
    }

    private fun inRange(distance: Double, range: Double) = distance <= range

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371000

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }
}
