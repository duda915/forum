package com.mdud.forum.user.authority

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.persistence.*

val passwordEncoder = BCryptPasswordEncoder()

@Entity
@Table(name = "user_authority")
data class UserAuthority(
        @Enumerated(EnumType.STRING)
        val authority: Authority
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}