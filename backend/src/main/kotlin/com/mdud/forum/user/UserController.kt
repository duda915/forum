package com.mdud.forum.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.validation.Valid

@RestController
@RequestMapping("/api/user")
class UserController @Autowired constructor(
        private val userService: UserService
) {

    @GetMapping
    fun getUser(principal: Principal): User {
        return userService.getUser(principal.name)
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody @Valid userDTO: UserDTO): User {
        return userService.addUser(userDTO)
    }
}