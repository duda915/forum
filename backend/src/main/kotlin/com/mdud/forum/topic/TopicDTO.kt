package com.mdud.forum.topic

import com.mdud.forum.topic.post.PostDTO
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

data class TopicDTO(
        var originalPoster: String = "",
        @get:NotEmpty
        val title: String,
        @get:Valid
        val posts: List<PostDTO>
)