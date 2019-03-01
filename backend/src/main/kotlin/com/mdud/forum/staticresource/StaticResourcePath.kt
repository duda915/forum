package com.mdud.forum.staticresource

import com.mdud.forum.configuration.Variables
import java.nio.file.Path
import java.nio.file.Paths

class StaticResourcePath (
        val fileName: String,
        val staticResourceType: StaticResourceType
) {
    fun getAbsolutePath(): Path {
        return Paths.get("${Variables.staticResourcesDir}/${staticResourceType.name.toLowerCase()}/$fileName")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StaticResourcePath

        if (fileName != other.fileName) return false
        if (staticResourceType != other.staticResourceType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fileName.hashCode()
        result = 31 * result + staticResourceType.hashCode()
        return result
    }


}