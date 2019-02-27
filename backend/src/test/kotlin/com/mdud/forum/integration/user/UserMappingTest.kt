package com.mdud.forum.integration.user

import com.mdud.forum.user.PasswordEncoder
import com.mdud.forum.user.User
import com.mdud.forum.user.UserRepository
import com.mdud.forum.user.authority.Authority
import com.mdud.forum.user.authority.UserAuthority
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.everyItem
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional


@RunWith(SpringRunner::class)
@SpringBootTest
@Transactional
@Ignore("rerun if user mappings change")
class UserMappingTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var user: User

    @Before
    fun setup() {
        val authorities = Authority.values().map { UserAuthority(it) }.toMutableSet()
        this.user = User("testuser", "password", "image", authorities)
    }

    @Test
    fun save() {
        val savedUser = userRepository.save(user)

        //multiple asserts because mappings are rarely changed
        assertNotNull(savedUser.id)
        assertTrue(PasswordEncoder.getInstance.matches("password", user.password))
        assertThat(savedUser.authorities.map { it.id }, everyItem(CoreMatchers.notNullValue()))
    }

    @Test
    fun saveRemoveAuthorityAndSave_ShouldRemoveAuthorityFromDB() {
        val savedUser = userRepository.save(user)
        savedUser.authorities.removeIf { it.authority == Authority.ADMIN }
        userRepository.save(savedUser)

        val userWithoutAdminAuthority = userRepository.findById(savedUser.id!!).get()
        assertThat(userWithoutAdminAuthority.authorities.map { it.authority }, everyItem(CoreMatchers.not(Authority.ADMIN)))
    }


}