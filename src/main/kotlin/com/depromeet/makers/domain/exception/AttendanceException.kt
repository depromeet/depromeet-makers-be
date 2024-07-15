package com.depromeet.makers.domain.exception

open class AttendanceException(
    errorCode: ErrorCode,
) : DomainException(errorCode)

class AttendanceBeforeTimeException : AttendanceException(ErrorCode.BEFORE_AVAILABLE_ATTENDANCE_TIME)
class AttendanceAfterTimeException : AttendanceException(ErrorCode.AFTER_AVAILABLE_ATTENDANCE_TIME)
class AttendanceAlreadyExistsException : AttendanceException(ErrorCode.ALREADY_ATTENDANCE)
class InvalidCheckInTimeException : AttendanceException(ErrorCode.INVALID_CHECKIN_TIME)
class MissingPlaceParamException : AttendanceException(ErrorCode.MISSING_PLACE_PARAM)
class InvalidCheckInDistanceException : AttendanceException(ErrorCode.INVALID_CHECKIN_DISTANCE)
class NotFoundAttendanceException : AttendanceException(ErrorCode.NOT_EXIST_ATTENDANCE)
class InvalidCheckInCodeException : AttendanceException(ErrorCode.INVALID_CHECKIN_CODE)
class NotSupportedCheckInCodeException : AttendanceException(ErrorCode.NOT_SUPPORTED_CHECKIN_CODE)
