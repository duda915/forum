package com.mdud.forum.staticresource

import com.mdud.forum.staticresource.util.StaticResourceLink
import com.mdud.forum.staticresource.util.StaticResourceType

interface StaticResourceService {

    fun addStaticResource(staticResourceType: StaticResourceType, byteArray: ByteArray): StaticResourceLink
    fun removeStaticResource(urlPath: String)
}