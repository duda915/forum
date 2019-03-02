package com.mdud.forum.user

import com.mdud.forum.user.authority.Authority
import com.mdud.forum.user.authority.UserAuthority
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.util.*
import kotlin.NoSuchElementException

@RunWith(SpringJUnit4ClassRunner::class)
class UserServiceImplTest {

    @InjectMocks
    private lateinit var userServiceImpl: UserServiceImpl

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var user: User

    @Before
    fun setup() {
        val authorities = Authority.values().map { UserAuthority(it) }.toHashSet()
        user = User("username", "password", "image", authorities)
    }

    @Test
    fun getUser_GetExistentUser_ShouldReturnUser() {
        `when`(userRepository.findUserByUsername("username")).thenReturn(Optional.of(user))
        val getUser = userServiceImpl.getUser("username")

        assertEquals(user, getUser)
        verify(userRepository, times(1)).findUserByUsername("username")
    }

    @Test(expected = NoSuchElementException::class)
    fun getUser_GetNonExistentUser_ShouldThrowException() {
        `when`(userRepository.findUserByUsername("username")).thenReturn(Optional.empty())
        userServiceImpl.getUser("username")
    }

    @Test
    fun addUser() {
        val userDTO = UserDTO("username", "password")
        val user = User(userDTO)
        `when`(userRepository.save(ArgumentMatchers.any(User::class.java))).then { it.getArgument(0) }

        val newUser = userServiceImpl.addUser(userDTO)

        assertEquals(user, newUser)
        verify(userRepository, times(1)).save(user)
    }

    @Test
    fun removeUser() {
        `when`(userRepository.findUserByUsername("username")).thenReturn(Optional.of(user))

        userServiceImpl.removeUser("username")

        verify(userRepository, times(1)).delete(user)
    }

    @Test
    fun changePassword() {
        `when`(userRepository.findUserByUsername("username")).thenReturn(Optional.of(user))
        `when`(userRepository.save(ArgumentMatchers.any(User::class.java))).then { it.getArgument(0) }

        val changedUser = userServiceImpl.changePassword(user.username, "newpassword")

        assertTrue(PasswordEncoder.getInstance.matches("newpassword", changedUser.password))
        verify(userRepository, times(1)).save(changedUser)
    }

    @Test
    fun setAuthorities() {
        val userDTO = UserDTO("username", "password")
        val user = User(userDTO)
        `when`(userRepository.findUserByUsername("username")).thenReturn(Optional.of(user))
        `when`(userRepository.save(ArgumentMatchers.any(User::class.java))).then { it.getArgument(0) }

        val newAuthorities = mutableSetOf(UserAuthority(Authority.ADMIN), UserAuthority(Authority.MODERATOR))

        val grantedUser = userServiceImpl.setAuthorities(user.username, newAuthorities)

        val expectedUser = User(userDTO)
        expectedUser.authorities = newAuthorities
        assertEquals(expectedUser, grantedUser)
        verify(userRepository, times(1)).save(expectedUser)
    }
}