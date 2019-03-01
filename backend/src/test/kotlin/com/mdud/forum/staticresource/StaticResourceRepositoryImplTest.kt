package com.mdud.forum.staticresource

import com.mdud.forum.configuration.Variables
import com.mdud.forum.configuration.VariablesConfiguration
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.nio.file.Paths

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(
        classes = [VariablesConfiguration::class,
            StaticResourceConfiguration::class]
)
class StaticResourceRepositoryImplTest {

    @Autowired
    private lateinit var variables: Variables

    @Autowired
    private lateinit var staticResourceRepository: StaticResourceRepository

    @Test
    fun save_ShouldSaveStaticResource() {
        val content = "test"
        val fileName = "teststatic"
        val path = Paths.get("${variables.userImagesResourceDir}/$fileName")

        val staticResource = StaticResource(path, content.toByteArray())
        staticResourceRepository.save(staticResource)

        val file = path.toFile()
        assertTrue(file.exists())

        file.delete()
    }
}