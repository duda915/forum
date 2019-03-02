package com.mdud.forum.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{username}")
    fun removeUser(@PathVariable(value = "username") username: String) {
        userService.removeUser(username)
    }

    @PostMapping
    fun changePassword(principal: Principal, @RequestBody @Valid passwordDTO: PasswordDTO) {
        val userDTO = UserDTO(principal.name, passwordDTO.password)
        userService.changePassword(userDTO)
    }

    @PutMapping
    fun changeUserImage(principal: Principal, @RequestParam("file") multipartFile: MultipartFile) {
        TODO("static resource service is not implemented")
    }
}