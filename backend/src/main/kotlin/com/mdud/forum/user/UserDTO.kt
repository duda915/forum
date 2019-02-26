package com.mdud.forum.user

import com.fasterxml.jackson.annotation.JsonIgnore

class UserDTO (
    val username: String,
    val password: String,
    val image: String
)