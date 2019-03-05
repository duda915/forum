package com.mdud.forum.topic

import com.mdud.forum.exception.AccessDeniedException
import com.mdud.forum.topic.post.Post
import com.mdud.forum.topic.post.PostDTO
import com.mdud.forum.user.User
import com.mdud.forum.user.UserService
import com.mdud.forum.user.authority.Authority
import com.mdud.forum.user.authority.UserAuthority
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.util.*
import kotlin.NoSuchElementException

@RunWith(SpringJUnit4ClassRunner::class)
class TopicServiceImplTest {

    @InjectMocks
    private lateinit var topicServiceImpl: TopicServiceImpl

    @Mock
    private lateinit var topicRepository: TopicRepository

    @Mock
    private lateinit var userService: UserService

    private lateinit var user: User
    private lateinit var firstPost: Post
    private lateinit var secondPost: Post
    private lateinit var topic: Topic

    @Before
    fun setup() {
        user = User("user", "user", "user", mutableSetOf(UserAuthority(Authority.USER)))

        firstPost = Post(user, "content")
        firstPost.id = 1
        secondPost = Post(user, "content2")
        secondPost.id = 2

        topic = Topic(user, "title", mutableListOf(firstPost, secondPost))
    }

    @Test
    fun getAllTopics() {
        `when`(topicRepository.findAll()).thenReturn(mutableListOf())
        topicServiceImpl.getAllTopics()

        verify(topicRepository, times(1)).findAll()
    }

    @Test(expected = NoSuchElementException::class)
    fun getTopic_GetNonExistentTopic_ShouldThrowException() {
        `when`(topicRepository.findById(1L)).thenReturn(Optional.empty())
        topicServiceImpl.getTopic(1L)
    }

    @Test
    fun getTopic_GetExistentTopic_ShouldReturnTopic() {
        `when`(topicRepository.findById(1L)).thenReturn(Optional.of(topic))

        val getTopic = topicServiceImpl.getTopic(1L)
        assertEquals(topic, getTopic)
        verify(topicRepository, times(1)).findById(1L)
    }

    @Test
    fun getPost_GetExistentPost_ShouldReturnPost() {
        `when`(topicRepository.findById(1L)).thenReturn(Optional.of(topic))

        val getPost = topicServiceImpl.getPost(1, 1)

        assertEquals(firstPost, getPost)
    }

    @Test(expected = NoSuchElementException::class)
    fun getPost_GetNonExistentPost_ShouldThrowException() {
        `when`(topicRepository.findById(1L)).thenReturn(Optional.empty())
        topicServiceImpl.getPost(1, 0)
    }

    @Test
    fun addTopic_AddTopicWithOnePost_ShouldAddTopic() {
        `when`(userService.getUser(user.username)).thenReturn(user)
        `when`(topicRepository.save(ArgumentMatchers.any(Topic::class.java))).then { it.getArgument(0) }

        val topicDTO = TopicDTO(user.username, "title", mutableListOf(PostDTO(user.username, "content")))
        val newTopic = topicServiceImpl.addTopic(topicDTO)

        val expectedTopic = Topic(user, "title", mutableListOf(firstPost))

        assertEquals(expectedTopic, newTopic)
        verify(topicRepository, times(1)).save(expectedTopic)
    }

    @Test(expected = IllegalArgumentException::class)
    fun addTopic_AddTopicWithZeroPosts_ShouldThrowException() {
        val topicDTO = TopicDTO(user.username, "title", mutableListOf(PostDTO(user.username, "content")))
        topicServiceImpl.addTopic(topicDTO)
    }

    @Test
    fun addPost() {
        `when`(userService.getUser(user.username)).thenReturn(user)
        `when`(topicRepository.findById(1L)).thenReturn(Optional.of(topic))
        `when`(topicRepository.save(ArgumentMatchers.any(Topic::class.java))).then { it.getArgument(0) }

        val postDTO = PostDTO(user.username, "content")
        val newPost = topicServiceImpl.addPost(1L, postDTO)

        val expectedPost = Post(user, "content")

        assertEquals(expectedPost, newPost)
        verify(topicRepository, times(1)).save(topic)
    }

    @Test
    fun removeTopic() {
        topic.id = 10
        `when`(topicRepository.findById(topic.id!!)).thenReturn(Optional.of(topic))

        topicServiceImpl.removeTopic(10)

        verify(topicRepository, times(1)).deleteById(topic.id!!)
    }

    @Test
    fun removePost() {
        `when`(topicRepository.findById(1)).thenReturn(Optional.of(topic))
        `when`(topicRepository.save(ArgumentMatchers.any(Topic::class.java))).then { it.getArgument(0) }

        val topic = topicServiceImpl.removePost(1, 1)

        val expectedTopic = Topic(user, "title", mutableListOf(secondPost))
        assertEquals(expectedTopic, topic)
        verify(topicRepository, times(1)).save(expectedTopic)
    }

    @Test
    fun editTopic() {
        `when`(topicRepository.findById(1)).thenReturn(Optional.of(topic))
        `when`(topicRepository.save(ArgumentMatchers.any(Topic::class.java))).then { it.getArgument(0) }

        val topic = topicServiceImpl.editTopic(1, "newtitle")

        val expectedTopic = Topic(user, "newtitle", mutableListOf(firstPost, secondPost))

        assertEquals(expectedTopic, topic)
        verify(topicRepository, times(1)).save(expectedTopic)
    }

    @Test
    fun editPost_EditPostAsOriginalUser_ShouldEditPost() {
        `when`(topicRepository.findById(1)).thenReturn(Optional.of(topic))
        `when`(topicRepository.save(ArgumentMatchers.any(Topic::class.java))).then { it.getArgument(0) }
        `when`(userService.getUser("user")).thenReturn(user)

        val post = topicServiceImpl.editPost(1, 1, PostDTO("user", "newcontent"))

        val expectedPost = Post(user, "newcontent")
        assertEquals(expectedPost, post)

        val expectedTopic = Topic(user, "title", mutableListOf(expectedPost, secondPost))
        verify(topicRepository, times(1)).save(expectedTopic)

    }

    @Test(expected = AccessDeniedException::class)
    fun editPost_EditPostAsAnotherNonModeratorUser_ShouldThrowException() {
        val nonModerator = User("nonmod", "user", "user", mutableSetOf(UserAuthority(Authority.USER)))
        `when`(topicRepository.findById(1)).thenReturn(Optional.of(topic))
        `when`(topicRepository.save(ArgumentMatchers.any(Topic::class.java))).then { it.getArgument(0) }
        `when`(userService.getUser("nonmod")).thenReturn(nonModerator)

        topicServiceImpl.editPost(1, 1, PostDTO("nonmod", "newcontent"))
    }

    @Test
    fun editPost_EditPostAsModerator_ShouldEditPost() {
        val moderator = User("moderator", "user", "user", mutableSetOf(UserAuthority(Authority.MODERATOR)))
        `when`(topicRepository.findById(1)).thenReturn(Optional.of(topic))
        `when`(topicRepository.save(ArgumentMatchers.any(Topic::class.java))).then { it.getArgument(0) }
        `when`(userService.getUser("moderator")).thenReturn(moderator)

        topicServiceImpl.editPost(1, 1, PostDTO("moderator", "newcontent"))
    }
}