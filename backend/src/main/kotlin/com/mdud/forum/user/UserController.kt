package com.mdud.forum.user

import com.mdud.forum.exception.FileTypeException
import com.mdud.forum.staticresource.StaticResourceService
import com.mdud.forum.staticresource.StaticResourceType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.security.Principal
import javax.imageio.ImageIO
import javax.validation.Valid

@RestController
@RequestMapping("/api/user")
class UserController @Autowired constructor(
        private val userService: UserService,
        private val staticResourceService: StaticResourceService
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

    @PostMapping("/image" ,consumes = ["image/png"])
    fun changeUserImage(principal: Principal, @RequestParam("file") multipartFile: MultipartFile): User {
        checkIfFileIsAnImage(multipartFile)
        val link = staticResourceService.addStaticResource(StaticResourceType.IMAGE, multipartFile.bytes)
        return userService.changeUserImage(principal.name, link)
    }

    private fun checkIfFileIsAnImage(multipartFile: MultipartFile): BufferedImage? {
        return ImageIO.read(multipartFile.inputStream) ?: throw FileTypeException("not an image")
    }
}