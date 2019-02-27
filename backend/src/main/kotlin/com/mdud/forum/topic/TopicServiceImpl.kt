package com.mdud.forum.topic

import com.mdud.forum.topic.post.Post
import com.mdud.forum.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

class TopicServiceImpl @Autowired constructor(
        private val topicRepository: TopicRepository,
        private val userService: UserService
) : TopicService {

    override fun getTopic(id: Long): Topic {
        return topicRepository.findById(id).orElseThrow()
    }

    override fun getPost(topicId: Long, postId: Long): Post {
        return getTopic(topicId)
                .posts.first { it.id == postId }
    }

    override fun addTopic(topic: TopicDTO): Topic {
        topic.posts.takeIf { it.size != 1 }?.run { throw IllegalArgumentException("new topic should contain one post") }

        val originalPoster = userService.getUser(topic.originalPoster)
        val newTopic = Topic(originalPoster, topic.title, mutableListOf(Post(originalPoster, topic.posts.first().content)))
        return topicRepository.save(newTopic)
    }

    override fun addPost(topicId: Long, post: PostDTO): Post {
        var topic = getTopic(topicId)
        val user = userService.getUser(post.poster)

        val newPost = Post(user, post.content)

        topic.posts.add(newPost)
        topic = topicRepository.save(topic)

        return topic.posts.last()
    }

    override fun removeTopic(topicId: Long) {
        topicRepository.deleteById(topicId)
    }

    override fun removePost(topicId: Long, postId: Long): Topic {
        val topic = getTopic(topicId)
        val removePost = getPost(topicId, postId)
        topic.posts.remove(removePost)

        return topicRepository.save(topic)
    }

    override fun editTopic(topicId: Long, title: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editPost(topicId: Long, postId: Long, post: PostDTO): Post {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}


