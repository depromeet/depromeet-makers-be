package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.exception.InvalidCheckInDistanceException
import com.depromeet.makers.domain.exception.InvalidCheckInTimeException
import com.depromeet.makers.domain.exception.MissingPlaceParamException
import com.depromeet.makers.domain.exception.NotFoundAttendanceException
import com.depromeet.makers.domain.gateway.AttendanceGateway
import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.Attendance
import java.time.DayOfWeek
import java.time.LocalDateTime
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class CheckInSession(
    private val attendanceGateway: AttendanceGateway,
    private val sessionGateway: SessionGateway,
    private val memberGateway: MemberGateway,
) : UseCase<CheckInSession.CheckInSessionInput, Attendance> {
    data class CheckInSessionInput(
        val now: LocalDateTime,
        val memberId: String,
        val longitude: Double,
        val latitude: Double,
    )

    override fun execute(input: CheckInSessionInput): Attendance {
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
        }.getOrElse { throw NotFoundAttendanceException() }

        attendance.isAvailableCheckInRequest(thisWeekSession.startTime, input.now)

        // 오프라인 세션의 경우 거리 확인 로직
        if (thisWeekSession.isOffline()) {
            validateLocation(input.latitude, input.longitude)

            val distance = calculateDistance(
                thisWeekSession.place!!.latitude,
                thisWeekSession.place!!.longitude,
                input.latitude,
                input.longitude,
            )

            if (inRange(distance, 1000.0).not()) { // 1000m 기준 (임시 설정)
                throw InvalidCheckInDistanceException()
            }
        }

        val expectAttendanceStatus = attendance.expectAttendanceStatus(thisWeekSession.startTime, input.now)
        return attendanceGateway.save(attendance.checkIn(input.now, expectAttendanceStatus))
    }

    private fun LocalDateTime.getMonday() = this.toLocalDate().with(DayOfWeek.MONDAY).atStartOfDay()

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

    private fun validateLocation(latitude: Double, longitude: Double) {
        if (latitude == 0.0 || longitude == 0.0) {
            throw MissingPlaceParamException()
        }
    }
}
