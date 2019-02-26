package com.mdud.forum.user

import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Long>