package com.depromeet.makers.util

import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority

object AuthenticationUtil {
    fun Authentication.isAdmin(): Boolean {
        return this.authorities.contains(SimpleGrantedAuthority("ROLE_ADMIN"))
    }
}
