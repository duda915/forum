package com.mdud.forum.staticresource

import com.mdud.forum.staticresource.util.StaticResourcePath

interface StaticResourceRepository {
    fun save(staticResource: StaticResource?): StaticResource
    fun delete(staticResource: StaticResource)
    fun findByPath(staticResourcePath: StaticResourcePath): StaticResource
}

