package com.mdud.forum.user

import com.mdud.forum.user.authority.UserAuthority
import com.mdud.forum.user.authority.passwordEncoder
import javax.persistence.*

@Entity
@Table(name = "forum_user")
class User(
        @Column(name = "username")
        val username: String,

        password: String,

        @Column(name = "image")
        var image: String,

        @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
        @JoinColumn(referencedColumnName = "forum_user_id")
        val authorities: MutableSet<UserAuthority>
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "password")
    var password: String = passwordEncoder.encode(password)
        set(value) {
            field = passwordEncoder.encode(value)
        }
}