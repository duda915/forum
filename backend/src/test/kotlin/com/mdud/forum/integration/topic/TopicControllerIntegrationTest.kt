package com.mdud.forum.integration.topic

import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import com.mdud.forum.integration.IntegrationProfileValueSource
import com.mdud.forum.integration.TokenTestHelper
import com.mdud.forum.topic.TopicDTO
import com.mdud.forum.topic.post.PostDTO
import org.hamcrest.CoreMatchers
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.IfProfileValue
import org.springframework.test.annotation.ProfileValueSourceConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
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
class TopicControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val controllerEndpoint = "/api/topic"
    private val objectMapper = ObjectMapper()

    @Test
    fun addTopic() {
        addTestTopic()
    }

    @Test
    fun getAllTopics() {
        addTestTopic()

        mockMvc.perform(get(controllerEndpoint)
                .headers(TokenTestHelper.getTokenHeader(mockMvc)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
    }

    @Test
    fun getTopic() {
        val ids = addTestTopic()

        mockMvc.perform(get("$controllerEndpoint/${ids[0]}")
                .headers(TokenTestHelper.getTokenHeader(mockMvc)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
    }

    @Test
    fun getPost() {
        val ids = addTestTopic()

        mockMvc.perform(get("$controllerEndpoint/${ids[0]}/post/${ids[1]}")
                .headers(TokenTestHelper.getTokenHeader(mockMvc)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
    }

    @Test
    fun addPost() {
        val ids = addTestTopic()

        val post = PostDTO("", "content")
        val json = objectMapper.writeValueAsString(post)

        mockMvc.perform(post("$controllerEndpoint/${ids[0]}/post")
                .headers(TokenTestHelper.getTokenHeader(mockMvc))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
    }

    @Test
    fun removeTopic() {
        val ids = addTestTopic()

        mockMvc.perform(delete("$controllerEndpoint/${ids[0]}")
                .headers(TokenTestHelper.getTokenHeader(mockMvc)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
    }

    @Test
    fun removePost() {
        val ids = addTestTopic()

        mockMvc.perform(delete("$controllerEndpoint/${ids[0]}/post/${ids[1]}")
                .headers(TokenTestHelper.getTokenHeader(mockMvc)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
    }

    @Test
    fun editTopic() {
        val ids = addTestTopic()

        mockMvc.perform(put("$controllerEndpoint/${ids[0]}")
                .headers(TokenTestHelper.getTokenHeader(mockMvc))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("newTitle"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
    }

    @Test
    fun editPost() {
        val ids = addTestTopic()
        val postDTO = PostDTO("", "newContent")
        val json = objectMapper.writeValueAsString(postDTO)

        mockMvc.perform(put("$controllerEndpoint/${ids[0]}/post/${ids[1]}")
                .headers(TokenTestHelper.getTokenHeader(mockMvc))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
    }


    private fun addTestTopic(): List<String> {
        val topicDTO = TopicDTO("", "NewTopic", mutableListOf(PostDTO("", "FirstPost")))
        val json = objectMapper.writeValueAsString(topicDTO)

        val result = mockMvc.perform(post(controllerEndpoint)
                .headers(TokenTestHelper.getTokenHeader(mockMvc))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.title", CoreMatchers.`is`("NewTopic")))
                .andExpect(jsonPath("$.posts[0].content", CoreMatchers.`is`("FirstPost")))
                .andReturn()

        return listOf(JsonPath.parse(result.response.contentAsString).read<Int>("$.id").toString(),
                JsonPath.parse(result.response.contentAsString).read<Int>("$.posts[0].id").toString())
    }


}