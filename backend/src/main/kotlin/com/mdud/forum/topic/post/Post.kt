package com.mdud.forum.topic.post

import com.mdud.forum.user.User
import javax.persistence.*

@Entity
@Table(name = "post")
data class Post(
        @ManyToOne(cascade = [CascadeType.MERGE])
        @JoinColumn(name = "poster")
        val poster: User = User(),

        @Column(name = "content")
        var content: String = ""
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}
