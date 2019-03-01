package com.mdud.forum.staticresource

interface StaticResourceService {

    fun addStaticResource(staticResourceType: StaticResourceType, byteArray: ByteArray): StaticResourceLink
    fun removeStaticResource(resourceEndpoint: String)
}