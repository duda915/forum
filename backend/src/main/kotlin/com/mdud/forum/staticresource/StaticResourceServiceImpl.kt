package com.mdud.forum.staticresource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class StaticResourceServiceImpl @Autowired constructor(
        private val staticResourceRepository: StaticResourceRepository
): StaticResourceService {

    override fun addStaticResource(staticResourceType: StaticResourceType, byteArray: ByteArray): StaticResourceLink {
        val fileName = UUID.randomUUID().toString()
        val staticResourcePath = StaticResourcePath(fileName, staticResourceType)
        val staticResource = StaticResource(staticResourcePath, byteArray)

        val savedResource = staticResourceRepository.save(staticResource)
        return StaticResourceLink(savedResource.staticResourcePath)
    }

    override fun removeStaticResource(resourceEndpoint: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

