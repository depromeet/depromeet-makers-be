package com.depromeet.makers.components

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine

@Component
class EmailSender(
    private val javaMailSender: JavaMailSender,
    private val springTemplateEngine: SpringTemplateEngine,
) {
    fun sendEmailVerificationMail(request: SendVerificationEmailRequest) {
        val message = javaMailSender.createMimeMessage()
        val content = with(Context()) {
            setVariable("code", request.code)
            springTemplateEngine.process("email-verification", this)
        }

        with(MimeMessageHelper(message, false)) {
            setTo(request.targetEmail)
            setSubject("[디프만] 회원 인증 메일입니다")
            setText(content, true)
            javaMailSender.send(message)
        }
    }

    data class SendVerificationEmailRequest(
        val targetEmail: String,
        val code: String,
    )
}
