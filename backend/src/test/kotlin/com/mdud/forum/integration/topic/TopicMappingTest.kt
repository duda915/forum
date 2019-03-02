package com.mdud.forum.integration.topic

import com.mdud.forum.topic.Topic
import com.mdud.forum.topic.TopicRepository
import com.mdud.forum.topic.post.Post
import com.mdud.forum.user.User
import com.mdud.forum.user.UserRepository
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest
@Transactional
@Ignore("rerun if post or topic mappings change")
class TopicMappingTest {

    @Autowired
    lateinit var topicRepository: TopicRepository

    @Autowired
    lateinit var userRepository: UserRepository

    private lateinit var adminUser: User
    private lateinit var moderator: User

    @Before
    fun before() {
        adminUser = userRepository.findUserByUsername("admin").orElseThrow()
        moderator = userRepository.findUserByUsername("moderator").orElseThrow()
    }

    @Test
    fun saveTopic_ShouldSaveTopicAndPosts() {
        val adminPosts = mutableListOf("firstpost", "secondpost").map { Post(adminUser, it) }.toMutableList()
        val moderatorPosts = mutableListOf("firstpost", "secondpost").map { Post(moderator, it) }.toMutableList()
        val posts = adminPosts.toMutableList()
        posts.addAll(moderatorPosts)

        val topic = Topic(adminUser, "NewTopic", posts)
        val newTopic = topicRepository.save(topic)

        //multiple asserts because mappings are rarely changed
        assertNotNull(newTopic.id)
        assertThat(newTopic.posts.map { it.id }, CoreMatchers.everyItem(notNullValue()))
        assertEquals(4, newTopic.posts.size)
    }

    @Test
    fun saveAndUpdateTopic_ShouldProperlyUpdateTopic() {
        val adminPosts = mutableListOf("firstpost", "secondpost").map { Post(adminUser, it) }.toMutableList()
        val moderatorPosts = mutableListOf("firstpost", "secondpost").map { Post(moderator, it) }.toMutableList()
        val posts = adminPosts.toMutableList()
        posts.addAll(moderatorPosts)

        val topic = Topic(adminUser, "NewTopic", posts)
        val newTopic = topicRepository.save(topic)
        newTopic.posts.removeIf { it.poster == moderator }
        topicRepository.save(newTopic)

        val getTopic = topicRepository.findById(newTopic.id!!).orElseThrow()

        assertEquals(2, getTopic.posts.size)
        assertThat(getTopic.posts.map { it.poster }, everyItem(`is`(adminUser)))
    }
}