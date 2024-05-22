package com.depromeet.makers

import com.depromeet.makers.domain.gateway.AlertGateway
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.info.BuildProperties
import org.springframework.context.ApplicationListener
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*

@Component
class ApplicationBootStrapEventListener(
    private val environment: Environment,
    private val buildProperties: BuildProperties,
    private val alertGateway: AlertGateway,
): ApplicationListener<ApplicationReadyEvent> {
    val serverDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private fun isProductionInstance(): Boolean {
        return environment.activeProfiles.contains("prod")
    }

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        if (!isProductionInstance()) return

        alertGateway.sendInfo(
            "백엔드 서버 부트스트랩" to "백엔드 서버가 준비되었습니다",
            "부트스트랩 소요 시간" to "${event.timeTaken.toSeconds()} 초",
            "이미지 빌드 시점" to buildProperties.time.atZone(ZoneId.of("Asia/Seoul")).toString(),
            "현재 서버 시간" to serverDateFormat.format(Date())
        )
    }
}
