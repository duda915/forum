package com.mdud.forum.integration

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.servlet.MockMvc

@RunWith(SpringJUnit4ClassRunner::class)
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


