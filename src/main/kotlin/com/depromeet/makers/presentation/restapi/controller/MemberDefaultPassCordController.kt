package com.depromeet.makers.presentation.restapi.controller

import com.depromeet.makers.domain.usecase.UpdateDefaultMemberPassCord
import com.depromeet.makers.presentation.restapi.dto.request.MemberDefaultPassCordRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth/default-passcord")
class MemberDefaultPassCordController(
    private val updateDefaultMemberPassCord: UpdateDefaultMemberPassCord,
) {
    @PostMapping
    fun setDefaultPassCord(
        @RequestBody @Valid request: MemberDefaultPassCordRequest
    ) {
        updateDefaultMemberPassCord.execute(
            UpdateDefaultMemberPassCord.UpdateDefaultMemberPassCordInput(
                email = request.email,
                passCord = request.passCord
            )
        )
    }
}
