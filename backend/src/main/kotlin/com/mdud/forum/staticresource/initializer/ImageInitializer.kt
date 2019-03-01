package com.mdud.forum.staticresource.initializer

import com.mdud.forum.configuration.Variables
import com.mdud.forum.initializer.BaseInitializer
import com.mdud.forum.staticresource.StaticResourceType
import org.slf4j.LoggerFactory
import java.io.File

class ImageInitializer : BaseInitializer() {
    private val logger = LoggerFactory.getLogger(ImageInitializer::class.java)

    override fun initialize() {
        val userImageDir = File("${Variables.staticResourcesDir}/${StaticResourceType.USER.name.toLowerCase()}")
        userImageDir.takeIf { !it.exists() }?.apply {
            userImageDir.mkdirs()
            logger.info("user image directory created")
        }
    }
}

