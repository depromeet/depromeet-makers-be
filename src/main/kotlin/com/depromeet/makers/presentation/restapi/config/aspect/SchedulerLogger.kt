package com.depromeet.makers.presentation.restapi.config.aspect

import com.depromeet.makers.util.logger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Aspect
@Component
class SchedulerLogger {
    val logger = logger()

    @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    fun logging(joinPoint: ProceedingJoinPoint): Any? {
        val start = System.currentTimeMillis()
        logger.info("Scheduler: ${joinPoint.target.javaClass.simpleName} start")

        val result = joinPoint.proceed()

        val end = System.currentTimeMillis()
        logger.info("Scheduler: ${joinPoint.target.javaClass.simpleName} end, duration: ${end - start}ms")

        return result
    }
}
