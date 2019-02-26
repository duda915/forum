package com.mdud.forum.integration.user

import com.mdud.forum.user.PasswordEncoder
import com.mdud.forum.user.User
import com.mdud.forum.user.UserRepository
import com.mdud.forum.user.authority.Authority
import com.mdud.forum.user.authority.UserAuthority
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.everyItem
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional


@RunWith(SpringRunner::class)
@SpringBootTest
@Transactional
class UserMappingTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun testUserMappings() {
        val authorities = Authority.values().map { UserAuthority(it) }.toMutableSet()
        val user = User("user", "password", "image", authorities)
        val savedUser = userRepository.save(user)

        assertNotNull(savedUser.id)
        assertTrue(PasswordEncoder.getInstance.matches("password", user.password))
        assertThat(savedUser.authorities.map { it.id }, everyItem(CoreMatchers.notNullValue()))
    }
}