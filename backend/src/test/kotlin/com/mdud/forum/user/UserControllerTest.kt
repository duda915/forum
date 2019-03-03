package com.mdud.forum.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.mdud.forum.staticresource.StaticResourceLink
import com.mdud.forum.staticresource.StaticResourcePath
import com.mdud.forum.staticresource.StaticResourceService
import com.mdud.forum.staticresource.StaticResourceType
import com.mdud.forum.user.authority.Authority
import com.mdud.forum.user.authority.UserAuthority
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.security.Principal

@RunWith(SpringJUnit4ClassRunner::class)
class UserControllerTest {

    @InjectMocks
    private lateinit var userController: UserController

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var staticResourceService: StaticResourceService

    private lateinit var mockMvc: MockMvc

    val controllerEndpoint = "/api/user"

    private lateinit var user: User
    private val objectMapper = ObjectMapper()

    @Value("classpath:testdata/testuserimage.png")
    private lateinit var testUserImage: Resource

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

    @Test
    fun changeUserImage_UploadImage_ShouldChangeImage() {
        val principal = Principal { "user" }

        val file = MockMultipartFile("file", testUserImage.file.readBytes())
        val imageLink = StaticResourceLink(StaticResourcePath("test", StaticResourceType.IMAGE))

        `when`(staticResourceService.addStaticResource(StaticResourceType.IMAGE, file.bytes)).thenReturn(imageLink)
        `when`(userService.changeUserImage("user", imageLink)).thenReturn(user)

        mockMvc.perform(multipart("$controllerEndpoint/image")
                .file(file)
                .contentType(MediaType.IMAGE_PNG_VALUE)
                .principal(principal))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)

        verify(staticResourceService, times(1)).addStaticResource(StaticResourceType.IMAGE, file.bytes)
        verify(userService, times(1)).changeUserImage("user", imageLink)
    }

    @Test
    fun changeUserImage_UploadString_ShouldReturnBadRequest() {
        val principal = Principal { "user" }

        val file = MockMultipartFile("file", "string".toByteArray())
        val imageLink = StaticResourceLink(StaticResourcePath("test", StaticResourceType.IMAGE))

        `when`(staticResourceService.addStaticResource(StaticResourceType.IMAGE, file.bytes)).thenReturn(imageLink)
        `when`(userService.changeUserImage("user", imageLink)).thenReturn(user)

        mockMvc.perform(multipart("$controllerEndpoint/image")
                .file(file)
                .contentType(MediaType.IMAGE_PNG_VALUE)
                .principal(principal))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest)
    }
}