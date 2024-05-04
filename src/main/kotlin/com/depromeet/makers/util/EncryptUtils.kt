package com.depromeet.makers.util

import org.springframework.security.crypto.bcrypt.BCrypt

object EncryptUtils {
    fun encrypt(passCord: String) = BCrypt.hashpw(passCord, BCrypt.gensalt())
    fun isMatch(passCord: String, hashedPw: String) = BCrypt.checkpw(passCord, hashedPw)
}
