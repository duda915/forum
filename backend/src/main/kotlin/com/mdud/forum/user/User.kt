package com.mdud.forum.user

import com.fasterxml.jackson.annotation.JsonIgnore
import com.mdud.forum.user.authority.Authority
import com.mdud.forum.user.authority.UserAuthority
import javax.persistence.*

@Entity
@Table(name = "forum_user")
class User(
        @Column(name = "username")
        val username: String = "",

        password: String = "",

        @Column(name = "image")
        var image: String = "",

        @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
        @JoinColumn(name = "forum_user_id")
        var authorities: MutableSet<UserAuthority> = mutableSetOf()
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (username != other.username) return false
        if (image != other.image) return false
        if (authorities != other.authorities) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + image.hashCode()
        result = 31 * result + authorities.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }


}

