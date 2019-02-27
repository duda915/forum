package com.mdud.forum.topic

import com.mdud.forum.topic.post.Post

interface TopicService {
    fun getTopic(id: Long): Topic
    fun getPost(topicId: Long, postId: Long): Post
    fun addTopic(topic: TopicDTO): Topic
    fun addPost(topicId: Long, post: PostDTO): Post
    fun removeTopic(topicId: Long)
    fun removePost(topicId: Long, postId: Long): Topic
    fun editTopic(topicId: Long, title: String): Topic
    fun editPost(topicId: Long, postId: Long, post: PostDTO): Post
}