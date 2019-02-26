package com.mdud.forum.user

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

    override fun removeUser(username: String): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changePassword(username: String, newPassword: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setAuthorities(username: String, authorities: MutableSet<UserAuthority>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}