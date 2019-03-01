package com.mdud.forum.staticresource

interface StaticResourceRepository {
    fun save(staticResource: StaticResource?): StaticResource
    fun delete(staticResource: StaticResource)
    fun findByPath(staticResourcePath: StaticResourcePath): StaticResource
}

