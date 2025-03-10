package com.depromeet.makers

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class DepromeetMakersBeApplication

fun main(args: Array<String>) {
    runApplication<DepromeetMakersBeApplication>(*args)
}
