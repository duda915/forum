package com.mdud.forum.staticresource

import com.mdud.forum.staticresource.util.StaticResourcePath

class StaticResource (
        val staticResourcePath: StaticResourcePath,
        val content: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StaticResource

        if (staticResourcePath != other.staticResourcePath) return false
        if (!content.contentEquals(other.content)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = staticResourcePath.hashCode()
        result = 31 * result + content.contentHashCode()
        return result
    }
}