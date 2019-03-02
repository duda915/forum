package com.mdud.forum.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserConfiguration @Autowired constructor(
        private val userService: UserService
) {
    @Bean
    fun userInitializer() : UserInitializer {
        return UserInitializer(userService)
    }

    @Bean
    fun passwordEncoder(): org.springframework.security.crypto.password.PasswordEncoder {
        return PasswordEncoder.getInstance
    }
}