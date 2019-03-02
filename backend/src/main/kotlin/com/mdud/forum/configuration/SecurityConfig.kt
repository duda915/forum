package com.mdud.forum.configuration

import com.mdud.forum.user.PasswordEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer

@Configuration
@EnableWebSecurity
@EnableAuthorizationServer
@EnableResourceServer
class SecurityConfig @Autowired constructor(
        @Qualifier("userDetailsServiceImpl") private val userDetailsService: UserDetailsService
) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailsService)?.passwordEncoder(PasswordEncoder.getInstance)
    }

    override fun configure(http: HttpSecurity?) {
        http?.authorizeRequests()?.anyRequest()?.authenticated()
    }


}