package com.mdud.forum.staticresource.util

import com.mdud.forum.configuration.Variables
import java.nio.file.Path
import java.nio.file.Paths

class StaticResourcePath (
        private val variables: Variables,
        val staticResourceRelativePath:String
) {
    fun getAbsolutePath(): Path {
        return Paths.get("${variables.staticResourcesDir}/$staticResourceRelativePath")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StaticResourcePath

        if (staticResourceRelativePath != other.staticResourceRelativePath) return false

        return true
    }

    override fun hashCode(): Int {
        return staticResourceRelativePath.hashCode()
    }


}