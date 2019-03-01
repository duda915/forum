package com.mdud.forum.image

import java.io.File

interface ImageRepository {
    fun save(image: Image): File
}

