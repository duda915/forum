package com.mdud.forum.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

class Variables {

    val home: String = System.getProperty("user.home")
    val userImagesResourceDir = "${home}/forum/user"

}

@Configuration
class VariablesConfiguration {
    @Bean
    fun variables(): Variables {
        return Variables()
    }
}
