package com.mdud.forum.topic

class TopicDTO (
        val originalPoster: String,
        val title: String,
        val posts: List<PostDTO>
)