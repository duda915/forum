package com.mdud.forum.staticresource

import com.mdud.forum.staticresource.util.StaticResourceType

interface StaticResourceService {

    fun addStaticResource(staticResourceType: StaticResourceType, byteArray: ByteArray): StaticResource
    fun removeStaticResource(urlPath: String)
}