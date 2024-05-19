package com.depromeet.makers.scheduler

import com.depromeet.makers.domain.usecase.UpdateAbsenceMember
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Component
class AbsentMemberScheduler(
    private val updateAbsenceMember: UpdateAbsenceMember
) {

    @Scheduled(cron = "0 0 20 ? * SAT")
    fun scheduling() {
        val today = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)
        updateAbsenceMember.execute(UpdateAbsenceMember.UpdateAbsenceMemberInput(today))
    }
}
