package com.mdud.forum.topic.post

import javax.validation.constraints.NotEmpty

data class PostDTO(
        var poster: String = "",
        @get:NotEmpty
        val content: String
)