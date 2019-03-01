package com.mdud.forum.staticresource

import java.io.File
import java.nio.file.Files

class StaticResourceRepositoryImpl : StaticResourceRepository {
    override fun save(staticResource: StaticResource): File {
        return Files.write(staticResource.path, staticResource.content).toFile()
    }
}