package com.mdud.forum.image

import java.io.File
import java.nio.file.Files

class ImageRepositoryImpl : ImageRepository {
    override fun save(image: Image): File {
        return Files.write(image.path, image.content).toFile()
    }
}