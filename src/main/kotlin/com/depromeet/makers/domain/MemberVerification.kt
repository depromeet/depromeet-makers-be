package com.depromeet.makers.domain

import com.depromeet.makers.domain.vo.Code
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("memberVerification")
class MemberVerification(
    @Id
    val key: MemberVerificationKey,
    val expiresAt: LocalDateTime,
) {
    fun isExpired(): Boolean = expiresAt.isBefore(LocalDateTime.now())

    companion object {
        private const val VERIFICATION_EXPIRE_MINUTES = 10L

        fun create(
            memberId: ObjectId,
            verificationCode: Code,
        ): MemberVerification {
            return MemberVerification(
                key = MemberVerificationKey(
                    memberId = memberId,
                    verificationCode = verificationCode.code,
                ),
                expiresAt = LocalDateTime.now().plusMinutes(VERIFICATION_EXPIRE_MINUTES),
            )
        }
    }
}

data class MemberVerificationKey(
    val memberId: ObjectId,
    val verificationCode: String,
)
