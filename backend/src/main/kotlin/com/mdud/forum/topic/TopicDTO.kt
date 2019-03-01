package com.mdud.forum.topic

import com.mdud.forum.topic.post.PostDTO

class TopicDTO (
        val originalPoster: String,
        val title: String,
        val posts: List<PostDTO>
)