package com.mdud.forum.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.mdud.forum.user.authority.Authority
import com.mdud.forum.user.authority.UserAuthority
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.security.Principal

@RunWith(MockitoJUnitRunner::class)
class UserControllerTest {

    @InjectMocks
    private lateinit var userController: UserController

    @Mock
    private lateinit var userService: UserService

    private lateinit var mockMvc: MockMvc

    val controllerEndpoint = "/api/user"

    private lateinit var user: User
    private val objectMapper = ObjectMapper()

    @Before
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build()
        user = User("user", "user", "user", mutableSetOf(UserAuthority(Authority.USER)))

    }

    @Test
    fun getUser() {
        val principal = Principal { "user" }
        Mockito.`when`(userService.getUser("user")).thenReturn(user)

        mockMvc.perform(get(controllerEndpoint)
                .principal(principal))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.username").value("user"))

        Mockito.verify(userService, times(1)).getUser("user")
    }

    @Test
    fun registerUser_ValidUser_ShouldRegisterUser() {
        val userDTO = UserDTO("user", "user")
        `when`(userService.addUser(userDTO)).thenReturn(user)

        val json = objectMapper.writeValueAsString(userDTO)

        mockMvc.perform(post("$controllerEndpoint/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.username").value("user"))

        verify(userService, times(1)).addUser(userDTO)
    }

    @Test
    fun registerUser_InvalidUser_ShouldReturn400() {
        val userDTO = UserDTO("", "")

        val json = objectMapper.writeValueAsString(userDTO)

        mockMvc.perform(post("$controllerEndpoint/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest)
    }

    @Test
    fun removeUser() {
        mockMvc.perform(delete("$controllerEndpoint/test"))
                .andExpect(status().isOk)

        verify(userService, times(1)).removeUser("test")
    }

    @Test
    fun changePassword_ValidPassword_ShouldChangePassword() {
        val principal = Principal { "user" }
        val passwordDTO = PasswordDTO("newpass")

        val json = objectMapper.writeValueAsString(passwordDTO)

        mockMvc.perform(post(controllerEndpoint)
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk)

        val expectedParam = UserDTO("user", passwordDTO.password)
        verify(userService, times(1)).changePassword(expectedParam)
    }

    @Test
    fun changePassword_InvalidPassword_ShouldReturn404() {
        val principal = Principal { "user" }
        val passwordDTO = PasswordDTO("")

        val json = objectMapper.writeValueAsString(passwordDTO)

        mockMvc.perform(post(controllerEndpoint)
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isBadRequest)
    }





















}