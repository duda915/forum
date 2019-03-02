package com.mdud.forum.user

import javax.validation.constraints.NotEmpty

data class UserDTO (
        @get:NotEmpty
    val username: String,
        @get:NotEmpty
    val password: String
)