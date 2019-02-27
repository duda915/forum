package com.mdud.forum.user

import com.mdud.forum.initializer.BaseInitializer
import com.mdud.forum.user.authority.Authority
import com.mdud.forum.user.authority.UserAuthority
import org.slf4j.LoggerFactory

class UserInitializer constructor(
        private val userService: UserService
) : BaseInitializer() {

    private val logger = LoggerFactory.getLogger(UserInitializer::class.java)

    override fun initialize() {
        initializeUser()
        initializeModerator()
        initializerAdmin()
    }

    private fun initializerAdmin() {
        try {
            userService.getUser("admin")
        } catch (e: NoSuchElementException) {
            val userDTO = UserDTO("admin", "admin", "admin")

            userService.addUser(userDTO)
            userService.setAuthorities("admin", Authority.values().map { UserAuthority(it) }.toHashSet())
            logger.info("default admin initialized")
        }
    }

    private fun initializeModerator() {
        try {
            userService.getUser("moderator")
        } catch (e: NoSuchElementException) {
            val userDTO = UserDTO("moderator", "moderator", "moderator")
            val moderatorAuthoritySet = mutableSetOf(UserAuthority(Authority.USER), UserAuthority(Authority.MODERATOR))

            userService.addUser(userDTO)
            userService.setAuthorities("moderator", moderatorAuthoritySet)
            logger.info("default moderator initialized")
        }
    }

    private fun initializeUser() {
        try {
            userService.getUser("user")

        } catch (e : NoSuchElementException) {
            val userDTO = UserDTO("user", "user", "user")

            userService.addUser(userDTO)
            logger.info("default user initialized")
        }
    }
}

