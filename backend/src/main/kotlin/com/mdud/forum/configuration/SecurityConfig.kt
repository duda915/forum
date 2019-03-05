package com.mdud.forum.configuration

import com.mdud.forum.user.PasswordEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter

@Configuration
@EnableWebSecurity
@EnableAuthorizationServer
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
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

@Configuration
class ResourceServerConfiguration : ResourceServerConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http?.authorizeRequests()?.antMatchers("/api/user/register")?.permitAll()
                ?.anyRequest()?.authenticated()
    }
}