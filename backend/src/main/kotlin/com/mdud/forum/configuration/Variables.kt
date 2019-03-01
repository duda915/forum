package com.mdud.forum.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

open class Variables {

    val staticResourcesDir: String = "${System.getProperty("user.home")}/forum"
    val userImagesResourceDir = "user"

}

@Configuration
class VariablesConfiguration {
    @Bean
    fun variables(): Variables {
        return Variables()
    }
}
