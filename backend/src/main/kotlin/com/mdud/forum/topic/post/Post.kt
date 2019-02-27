package com.mdud.forum.topic.post

import com.mdud.forum.user.User
import javax.persistence.*

@Entity
@Table(name = "post")
data class Post(
        @ManyToOne(cascade = [CascadeType.MERGE])
        @JoinColumn(referencedColumnName = "poster")
        val poster: User = User(),

        @Column(name = "content")
        val content: String = ""
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}

@Entity
@Table(name = "topic")
data class Topic(
        @ManyToOne(cascade = [CascadeType.MERGE])
        @JoinColumn(referencedColumnName = "original_poster")
        val originalPoster: User = User(),

        @Column(name = "title")
        val title: String = "",

        @OneToMany(cascade = [CascadeType.ALL])
        @JoinColumn(name = "topic_id")
        val posts: MutableList<Post> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}