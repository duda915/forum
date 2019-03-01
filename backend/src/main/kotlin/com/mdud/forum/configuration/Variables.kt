package com.mdud.forum.configuration

class Variables {
    val home: String = System.getProperty("user.home")
    val userImagesResourceDir = "${home}/forum/user"
}