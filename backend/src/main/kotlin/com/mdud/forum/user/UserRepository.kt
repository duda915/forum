package com.mdud.forum.user

import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository : CrudRepository<User, Long> {
    fun findUserByUsername(username: String): Optional<User>
}