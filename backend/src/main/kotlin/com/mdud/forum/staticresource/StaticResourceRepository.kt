package com.mdud.forum.staticresource

import java.io.File
import java.nio.file.Path

interface StaticResourceRepository {
    fun save(staticResource: StaticResource): StaticResource
    fun delete(staticResource: StaticResource)
    fun findByPath(staticResourcePath: StaticResourcePath): StaticResource
}

