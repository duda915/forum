package com.mdud.forum.integration

import com.jayway.jsonpath.JsonPath
import org.springframework.http.HttpHeaders
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap

object TokenTestHelper {
    private const val tokenEndpoint = "/oauth/token"

    fun getTokenHeader(mockMvc: MockMvc): HttpHeaders {
        val params = LinkedMultiValueMap<String, String>()
        params.add("grant_type", "password")
        params.add("password", "admin")
        params.add("username", "admin")

        val result = mockMvc.perform(MockMvcRequestBuilders.post(tokenEndpoint)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("forum", "forum"))
                .params(params))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        val stringResponse = result.response.contentAsString

        val accessToken = JsonPath.parse(stringResponse).read("$.access_token", String::class.java)

        println(accessToken)

        val headers = LinkedMultiValueMap<String, String>()
        headers.add("Authorization", "Bearer $accessToken")

        return HttpHeaders(headers)
    }



}