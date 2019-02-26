package com.mdud.forum.user

import com.fasterxml.jackson.annotation.JsonIgnore
import com.mdud.forum.user.authority.Authority
import com.mdud.forum.user.authority.UserAuthority
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
        @JoinColumn(name = "forum_user_id")
        val authorities: MutableSet<UserAuthority>
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @JsonIgnore
    @Column(name = "password")
    var password: String = PasswordEncoder.getInstance.encode(password)
        set(value) {
            field = PasswordEncoder.getInstance.encode(value)
        }

    constructor(userDTO: UserDTO)
            : this(userDTO.username, userDTO.password, userDTO.image, mutableSetOf(UserAuthority(Authority.USER)))
}

