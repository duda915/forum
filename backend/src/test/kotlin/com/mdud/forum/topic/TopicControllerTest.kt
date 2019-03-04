package com.mdud.forum.topic

import com.fasterxml.jackson.databind.ObjectMapper
import com.mdud.forum.topic.post.Post
import com.mdud.forum.topic.post.PostDTO
import com.mdud.forum.user.User
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.security.Principal

@RunWith(SpringJUnit4ClassRunner::class)
class TopicControllerTest {

    @InjectMocks
    private lateinit var topicController: TopicController

    @Mock
    private lateinit var topicService: TopicService

    private lateinit var mockMvc: MockMvc

    private lateinit var topic: Topic

    private val controllerEndpoint = "/api/topic"

    private val objectMapper = ObjectMapper()

    @Before
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(topicController).build()

        topic = Topic(User(), "topic", mutableListOf(Post()))
    }

    @Test
    fun getTopic() {
        `when`(topicService.getTopic(1L)).thenReturn(topic)

        mockMvc.perform(get("$controllerEndpoint/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.title", CoreMatchers.`is`("topic")))

        verify(topicService, times(1)).getTopic(1)
    }

    @Test
    fun getPost() {
        `when`(topicService.getPost(1, 1)).thenReturn(Post(User(), "post"))

        mockMvc.perform(get("$controllerEndpoint/1/post/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.content", CoreMatchers.`is`("post")))

        verify(topicService, times(1)).getPost(1, 1)
    }

    @Test
    fun addTopic_AddValidTopic_ShouldAddValidTopic() {
        val principal = Principal { "principal" }
        val topicDTO = TopicDTO("", "newtopic", mutableListOf(PostDTO("", "test")))
        val json = objectMapper.writeValueAsString(topicDTO)

        val expectedDTO = TopicDTO("principal", "newtopic", mutableListOf(PostDTO("principal", "test")))
        `when`(topicService.addTopic(expectedDTO)).thenAnswer { it ->
            val topic = it.getArgument<TopicDTO>(0)
            Topic(User(topic.originalPoster), topic.title, topic.posts.map { Post(User(it.poster), it.content) }.toMutableList())
        }

        mockMvc.perform(post(controllerEndpoint)
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.originalPoster.username", CoreMatchers.`is`("principal")))
                .andExpect(jsonPath("$.posts[0].poster.username", CoreMatchers.`is`("principal")))

        verify(topicService, times(1)).addTopic(expectedDTO)
    }

    @Test
    fun addTopic_AddTopicWithInvalidTitle_ShouldReturnBadRequest() {
        val principal = Principal { "principal" }
        val topicDTO = TopicDTO("", "", mutableListOf(PostDTO("", "test")))
        val json = objectMapper.writeValueAsString(topicDTO)

        mockMvc.perform(post(controllerEndpoint)
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest)
    }

    @Test
    fun addTopic_AddTopicWithInvalidPostContent_ShouldReturnBadRequest() {
        val principal = Principal { "principal" }
        val topicDTO = TopicDTO("", "title", mutableListOf(PostDTO("", "")))
        val json = objectMapper.writeValueAsString(topicDTO)

        mockMvc.perform(post(controllerEndpoint)
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest)
    }

    @Test
    fun addPost_AddValidPost_ShouldReturnOk() {
        val principal = Principal { "principal" }
        val postDTO = PostDTO("", "postcontent")
        val json = objectMapper.writeValueAsString(postDTO)

        val expectedPostDTO = PostDTO("principal", "postcontent")
        `when`(topicService.addPost(1, expectedPostDTO)).thenAnswer {
            val post = it.getArgument<PostDTO>(1)
            Post(User(post.poster), post.content)
        }

        mockMvc.perform(post("$controllerEndpoint/1/post")
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.poster.username", CoreMatchers.`is`("principal")))

        verify(topicService, times(1)).addPost(1, expectedPostDTO)
    }

    @Test
    fun addPost_AddInvalidPost_ShouldReturnBadRequest() {
        val principal = Principal { "principal" }
        val postDTO = PostDTO("", "")
        val json = objectMapper.writeValueAsString(postDTO)


        mockMvc.perform(post("$controllerEndpoint/1/post")
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest)

    }

    @Test
    fun removeTopic() {
        mockMvc.perform(delete("$controllerEndpoint/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)

        verify(topicService, times(1)).removeTopic(1)
    }

    @Test
    fun removePost() {
        mockMvc.perform(delete("$controllerEndpoint/1/post/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)

        verify(topicService, times(1)).removePost(1, 1)
    }

    @Test
    fun editTopic_WithValidName_ShouldReturnOK() {
        val newTitle = "newtitle"

        `when`(topicService.editTopic(1, "newtitle")).thenReturn(Topic(User(), "newtitle"))

        mockMvc.perform(put("$controllerEndpoint/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(newTitle.toByteArray()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.title", CoreMatchers.`is`("newtitle")))

        verify(topicService, times(1)).editTopic(1, "newtitle")
    }


    @Test
    fun editTopic_WithInvalidName_ShouldReturnBadRequest() {
        val newTitle = ""

        mockMvc.perform(put("$controllerEndpoint/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(newTitle.toByteArray()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest)
    }

    @Test
    fun editPost_EditPostWithValidContent_ShouldReturnOK() {
        val principal = Principal { "test" }
        val postDTO = PostDTO("", "newcontent")
        val json = objectMapper.writeValueAsString(postDTO)

        val expectedPostDTO = PostDTO("test", postDTO.content)
        `when`(topicService.editPost(1, 1, expectedPostDTO)).thenReturn(Post(User(), postDTO.content))

        mockMvc.perform(put("$controllerEndpoint/1/post/1")
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.content", CoreMatchers.`is`(postDTO.content)))

        verify(topicService, times(1)).editPost(1, 1, expectedPostDTO)
    }


    @Test
    fun editPost_EditPostWithInvalidContent_ShouldReturnBadRequest() {
        val principal = Principal { "test" }
        val postDTO = PostDTO("", "")
        val json = objectMapper.writeValueAsString(postDTO)

        mockMvc.perform(put("$controllerEndpoint/1/post/1")
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest)
    }
}