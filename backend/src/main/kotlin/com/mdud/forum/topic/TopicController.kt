package com.mdud.forum.topic

import com.mdud.forum.topic.post.Post
import com.mdud.forum.topic.post.PostDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

//TODO validator

@RestController
@RequestMapping("/api/topic")
class TopicController @Autowired constructor(
        private val topicService: TopicService
) {

    @GetMapping("/{topicId}")
    fun getTopic(@PathVariable("topicId") topicId: Long): Topic {
        return topicService.getTopic(topicId)
    }

    @GetMapping("/{topicId}/post/{postId}")
    fun getPost(@PathVariable("topicId") topicId: Long, @PathVariable("postId") postId: Long): Post {
        return topicService.getPost(topicId, postId)
    }

    @PostMapping
    fun addTopic(principal: Principal, @RequestBody topicDTO: TopicDTO): Topic {
        topicDTO.originalPoster = principal.name
        topicDTO.posts.first().poster = principal.name

        return topicService.addTopic(topicDTO)
    }

    @PostMapping("/{topicId}/post")
    fun addPost(principal: Principal, @PathVariable("topicId") topicId: Long, @RequestBody postDTO: PostDTO): Post {
        postDTO.poster = principal.name

        return topicService.addPost(topicId, postDTO)
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    @DeleteMapping("/{topicId}")
    fun removeTopic(@PathVariable("topicId") topicId: Long) {
        topicService.removeTopic(topicId)
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    @DeleteMapping("/{topicId}/post/{postId}")
    fun removePost(@PathVariable("topicId") topicId: Long, @PathVariable("postId") postId: Long) {
        topicService.removePost(topicId, postId)
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    @PutMapping("/{topicId}")
    fun editTopic(@PathVariable("topicId") topicId: Long, @RequestBody title: String): Topic {
        return topicService.editTopic(topicId, title)
    }

    @PutMapping("/{topicId}/post/{postId}")
    fun editPost(principal: Principal, @PathVariable("topicId") topicId: Long,
                 @PathVariable("postId") postId: Long, @RequestBody postDTO: PostDTO): Post {
        postDTO.poster = principal.name

        return topicService.editPost(topicId, postId, postDTO)
    }
}