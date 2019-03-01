package com.mdud.forum.staticresource

import com.mdud.forum.configuration.Variables
import javax.persistence.AttributeConverter
import javax.persistence.Converter

class StaticResourceLink(
        staticResourcePath: StaticResourcePath
) {
    val resourceLink: String = setResourceLink(staticResourcePath)

    private fun setResourceLink(staticResourcePath: StaticResourcePath): String {
        val staticType = staticResourcePath.staticResourceType.name.toLowerCase()
        val fileName = staticResourcePath.fileName

        return "${Variables.staticEndpoint}/$staticType/$fileName"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StaticResourceLink

        if (resourceLink != other.resourceLink) return false

        return true
    }

    override fun hashCode(): Int {
        return resourceLink.hashCode()
    }


}