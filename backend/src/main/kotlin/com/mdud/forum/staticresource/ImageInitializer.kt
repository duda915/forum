package com.mdud.forum.staticresource

import com.mdud.forum.configuration.Variables
import com.mdud.forum.initializer.BaseInitializer
import org.slf4j.LoggerFactory
import java.io.File

class ImageInitializer(
        private val variables: Variables
) : BaseInitializer() {
    private val logger = LoggerFactory.getLogger(ImageInitializer::class.java)

    override fun initialize() {
        val userImageDir = File(variables.userImagesResourceDir)
        userImageDir.takeIf { !it.exists() }?.apply {
            userImageDir.mkdirs()
            logger.info("user image directory created")
        }
    }
}

