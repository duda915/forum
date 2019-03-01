package com.mdud.forum.staticresource

import java.io.File

interface StaticResourceRepository {
    fun save(staticResource: StaticResource): File
}

