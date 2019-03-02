package com.mdud.forum.user

import com.mdud.forum.user.authority.UserAuthority

interface UserService {
    fun getUser(username: String): User
    fun addUser(userDTO: UserDTO): User
    fun removeUser(username: String)
    fun changePassword(userDTO: UserDTO): User
    fun setAuthorities(username: String, authorities: MutableSet<UserAuthority>): User
}

