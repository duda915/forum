package com.mdud.forum.staticresource

import com.mdud.forum.configuration.Variables
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StaticResourceConfiguration @Autowired constructor(
        private val variables: Variables
) {

    @Bean
    fun imageInitializer(): ImageInitializer {
        return ImageInitializer(variables)
    }

    @Bean
    fun staticResourceRepository(): StaticResourceRepository {
        return StaticResourceRepositoryImpl()
    }
}