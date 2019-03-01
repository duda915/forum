package com.mdud.forum.staticresource

import com.mdud.forum.staticresource.util.StaticResourcePath
import java.nio.file.Files

class StaticResourceRepositoryImpl : StaticResourceRepository {
    override fun delete(staticResource: StaticResource) {
        findByPath(staticResource.staticResourcePath).staticResourcePath.getAbsolutePath().toFile().delete()
    }

    override fun findByPath(staticResourcePath: StaticResourcePath): StaticResource {
        val file = staticResourcePath.getAbsolutePath().toFile().takeIf { !it.isDirectory || !it.exists() }
                ?: throw NoSuchElementException("not a file")

        return StaticResource(staticResourcePath, file.readBytes())
    }

    override fun save(staticResource: StaticResource?): StaticResource {
        Files.write(staticResource!!.staticResourcePath.getAbsolutePath(), staticResource.content)
        return findByPath(staticResource.staticResourcePath)
    }
}

