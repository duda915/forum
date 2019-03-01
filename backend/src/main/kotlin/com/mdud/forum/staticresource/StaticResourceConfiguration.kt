package com.mdud.forum.staticresource

import com.mdud.forum.staticresource.initializer.ImageInitializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StaticResourceConfiguration {

    @Bean
    fun imageInitializer(): ImageInitializer {
        return ImageInitializer()
    }

    @Bean
    fun staticResourceRepository(): StaticResourceRepository {
        return StaticResourceRepositoryImpl()
    }
}