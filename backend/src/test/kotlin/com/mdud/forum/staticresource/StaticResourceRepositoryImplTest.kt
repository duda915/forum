package com.mdud.forum.staticresource

import com.mdud.forum.configuration.Variables
import com.mdud.forum.configuration.VariablesConfiguration
import org.junit.Assert.*
import org.junit.Before
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

    lateinit var content: String
    lateinit var path: StaticResourcePath

    @Before
    fun setup() {
        content = "test"
        val fileName = "teststatic"
        path = StaticResourcePath(variables, "${variables.userImagesResourceDir}/$fileName")
    }

    @Test
    fun save_SaveStaticResource_ShouldSaveNewFile() {
        val staticResource = StaticResource(path, content.toByteArray())

        val newResource = staticResourceRepository.save(staticResource)

        assertTrue(newResource.staticResourcePath.getAbsolutePath().toFile().exists())
    }

    @Test
    fun save_SaveStaticResource_NewFileContentShouldNotChange() {
        val staticResource = StaticResource(path, content.toByteArray())

        val newResource = staticResourceRepository.save(staticResource)

        assertEquals(staticResource, newResource)
    }

    @Test
    fun delete_AddThenDeleteStaticResource_ShouldDeleteStaticResource() {
        val staticResource = StaticResource(path, content.toByteArray())

        staticResourceRepository.save(staticResource)
        staticResourceRepository.delete(staticResource)

        assertFalse(staticResource.staticResourcePath.getAbsolutePath().toFile().exists())
    }

    @Test
    fun findByPath_AddThenFindStaticResource_ShouldReturnStaticResource() {
        val staticResource = StaticResource(path, content.toByteArray())
        staticResourceRepository.save(staticResource)

        val findResource = staticResourceRepository.findByPath(staticResource.staticResourcePath)

        assertEquals(staticResource, findResource)
    }


}