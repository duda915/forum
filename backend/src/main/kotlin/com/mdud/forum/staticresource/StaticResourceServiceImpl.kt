package com.mdud.forum.staticresource

import com.mdud.forum.configuration.Variables
import com.mdud.forum.staticresource.util.StaticResourcePath
import com.mdud.forum.staticresource.util.StaticResourceType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class StaticResourceServiceImpl @Autowired constructor(
        private val staticResourceRepository: StaticResourceRepository,
        private val variables: Variables
): StaticResourceService {

    override fun addStaticResource(staticResourceType: StaticResourceType, byteArray: ByteArray): StaticResource {
        val filePath = "${staticResourceType.name.toLowerCase()}/${UUID.randomUUID()}"
        val staticResourcePath = StaticResourcePath(variables, filePath)
        val staticResource = StaticResource(staticResourcePath, byteArray)

        return staticResourceRepository.save(staticResource)
    }

    override fun removeStaticResource(urlPath: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

