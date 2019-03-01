package com.mdud.forum.staticresource

import com.mdud.forum.configuration.Variables
import java.nio.file.Path
import java.nio.file.Paths

class StaticResourcePath(
        val fileName: String,
        val staticResourceType: StaticResourceType
) {

    fun getAbsolutePath(): Path {
        return Paths.get("${Variables.staticResourcesDir}/${staticResourceType.name.toLowerCase()}/$fileName")
    }

    companion object {
        fun createFromString(resourceLink: String): StaticResourcePath {
            val resourcePath = resourceLink.takeIf { it.contains("${Variables.staticEndpoint}/") }
                    ?.removePrefix("${Variables.staticEndpoint}/")
                    ?: throw IllegalArgumentException("not an endpoint")

            val fileNameAndTypeList = resourcePath.split("/")
                    .takeIf { it.size == 2 && it.all { split -> split.isNotEmpty() } }
                    ?: throw IllegalArgumentException("not and endpoint")

            return StaticResourcePath(fileNameAndTypeList.last(), StaticResourceType.valueOf(fileNameAndTypeList.first().toUpperCase()))
        }
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