package com.mdud.forum.user

import javax.validation.constraints.NotEmpty

data class PasswordDTO (
        @get:NotEmpty
        val password: String
)