package com.mdud.forum.user.authority

import javax.persistence.*

@Entity
@Table(name = "user_authority")
data class UserAuthority(
        @Enumerated(EnumType.STRING)
        val authority: Authority = Authority.USER
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}