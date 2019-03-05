package com.mdud.forum.integration.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.mdud.forum.integration.IntegrationProfileValueSource
import com.mdud.forum.integration.TokenTestHelper
import com.mdud.forum.user.UserDTO
import org.hamcrest.CoreMatchers
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.annotation.IfProfileValue
import org.springframework.test.annotation.ProfileValueSourceConfiguration
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ProfileValueSourceConfiguration(value = IntegrationProfileValueSource::class)
@IfProfileValue(name = "integration", value = "true")
class UserControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val controllerEndpoint = "/api/user"

    private val objectMapper = ObjectMapper()

    @Test
    fun getUser() {
        mockMvc.perform(get(controllerEndpoint)
                .headers(TokenTestHelper.getTokenHeader(mockMvc)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.username", CoreMatchers.`is`("admin")))
    }

    @Test
    fun registerUser() {
        val userDTO = UserDTO("registeruser", "registeruser")
        val json = objectMapper.writeValueAsString(userDTO)

        mockMvc.perform(post("$controllerEndpoint/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.username", CoreMatchers.`is`("registeruser")))
    }
}