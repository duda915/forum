package com.mdud.forum.topic

import com.mdud.forum.topic.post.Post

interface TopicService {
    fun getTopic(id: Long): Topic
    fun getPost(id: Long): Post
    fun addTopic(topic: Topic, post: Post): Topic
    fun addPost(topicId: Long, post: Post): Post
    fun removeTopic(topicId: Long)
    fun removePost(topicId: Long, postId: Long)
    fun editTopic(topicId: Long, title: String)
    fun editPost(topicId: Long, postId: Long, post: Post): Post
}