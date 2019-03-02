package com.mdud.forum.configuration

import com.mdud.forum.user.PasswordEncoder
import com.mdud.forum.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsServiceImpl @Autowired constructor(
        private val userService: UserService
) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(username: String?): UserDetails {
        username ?: throw UsernameNotFoundException("user $username not found")
        val appUser = userService.getUser(username)

        val authorities = appUser.authorities.map { SimpleGrantedAuthority(it.authority.name) }.toMutableSet()

        return User.builder()
                .username(appUser.username)
                .password(appUser.password)
                .authorities(authorities)
                .build()
    }

}