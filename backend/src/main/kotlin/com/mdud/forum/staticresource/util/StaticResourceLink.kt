package com.mdud.forum.staticresource.util

class StaticResourceLink(
        staticResourcePath: StaticResourcePath
) {
    val resourceLink: String = setResourceLink(staticResourcePath)

    private fun setResourceLink(staticResourcePath: StaticResourcePath): String {
        val staticEndpoints = staticResourcePath.variables.staticEndpoint
        val staticType = staticResourcePath.staticResourceType.name.toLowerCase()
        val fileName = staticResourcePath.fileName

        return "$staticEndpoints/$staticType/$fileName"
    }
}