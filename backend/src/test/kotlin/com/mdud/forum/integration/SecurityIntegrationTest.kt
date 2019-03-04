package com.mdud.forum.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun testOauthTokenEndpoint() {
        TokenTestHelper.getTokenHeader(mockMvc)
    }
}

object TokenTestHelper {
    private val tokenEndpoint = "/oauth/token"

    fun getTokenHeader(mockMvc: MockMvc): HttpHeaders {
        val params = LinkedMultiValueMap<String, String>()
        params.add("grant_type", "password")
        params.add("password", "admin")
        params.add("username", "admin")

        val result = mockMvc.perform(post(tokenEndpoint)
                .with(httpBasic("forum", "forum"))
                .params(params))
                .andExpect(status().isOk)
                .andReturn()

        val stringResponse = result.response.contentAsString

        val accessToken = JsonPath.parse(stringResponse).read("$.access_token", String::class.java)

        println(accessToken)

        val headers = LinkedMultiValueMap<String, String>()
        headers.add("Authorization", "Bearer $accessToken")

        return HttpHeaders(headers)
    }



}