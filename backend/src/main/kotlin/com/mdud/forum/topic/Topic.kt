package com.mdud.forum.topic

import com.mdud.forum.topic.post.Post
import com.mdud.forum.user.User
import javax.persistence.*

@Entity
@Table(name = "topic")
data class Topic(
        @ManyToOne(cascade = [CascadeType.MERGE])
        @JoinColumn(name = "original_poster")
        val originalPoster: User = User(),

        @Column(name = "title")
        var title: String = "",

        @OneToMany(cascade = [CascadeType.ALL])
        @JoinColumn(name = "topic_id")
        val posts: MutableList<Post> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}

