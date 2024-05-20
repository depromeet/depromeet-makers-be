package com.depromeet.makers

import com.depromeet.makers.domain.usecase.UseCase
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.FilterType
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@EnableAspectJAutoProxy
@SpringBootApplication
@ComponentScan(
    includeFilters = [ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = [UseCase::class]
    )]
)
class DepromeetMakersBeApplication

fun main(args: Array<String>) {
    runApplication<DepromeetMakersBeApplication>(*args)
}
