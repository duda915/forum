package com.mdud.forum.user

import com.mdud.forum.staticresource.StaticResourceLink
import com.mdud.forum.user.authority.UserAuthority
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl @Autowired constructor(
        private val userRepository: UserRepository
) : UserService {

    override fun getUser(username: String): User {
        return userRepository.findUserByUsername(username).orElseThrow()
    }

    override fun addUser(userDTO: UserDTO): User {
        val user = User(userDTO)
        return userRepository.save(user)
    }

    override fun removeUser(username: String) {
        val user = getUser(username)
        userRepository.delete(user)
    }

    override fun changePassword(userDTO: UserDTO): User {
        val user = getUser(userDTO.username)
        user.password = userDTO.password
        return userRepository.save(user)
    }

    override fun setAuthorities(username: String, authorities: MutableSet<UserAuthority>): User {
        val user = getUser(username)
        user.authorities = authorities
        return userRepository.save(user)
    }

    override fun changeUserImage(username: String, staticResourceLink: StaticResourceLink): User {
        val user = getUser(username)
        user.image = staticResourceLink.resourceLink
        return userRepository.save(user)
    }
}