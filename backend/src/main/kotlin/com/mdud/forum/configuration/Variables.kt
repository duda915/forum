package com.mdud.forum.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

object Variables {

    val staticResourcesDir: String = "${System.getProperty("user.home")}/forum"

    const val staticEndpoint = "localhost:8080/static"

}