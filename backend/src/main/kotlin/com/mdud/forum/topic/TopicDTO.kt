package com.mdud.forum.topic

import com.mdud.forum.topic.post.PostDTO

data class TopicDTO(
        var originalPoster: String = "",
        val title: String,
        val posts: List<PostDTO>
)