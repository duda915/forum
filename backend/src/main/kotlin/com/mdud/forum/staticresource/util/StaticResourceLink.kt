package com.mdud.forum.staticresource.util

import com.mdud.forum.configuration.Variables

class StaticResourceLink(
        staticResourcePath: StaticResourcePath
) {
    val resourceLink: String = setResourceLink(staticResourcePath)

    private fun setResourceLink(staticResourcePath: StaticResourcePath): String {
        val staticType = staticResourcePath.staticResourceType.name.toLowerCase()
        val fileName = staticResourcePath.fileName

        return "${Variables.staticEndpoint}/$staticType/$fileName"
    }
}