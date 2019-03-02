package com.mdud.forum.user

import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object PasswordEncoder {
    val getInstance = BCryptPasswordEncoder()
}
