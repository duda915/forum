package com.mdud.forum.staticresource

import com.mdud.forum.staticresource.util.StaticResourcePath
import com.mdud.forum.staticresource.util.StaticResourceType
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(
        classes = [StaticResourceConfiguration::class]
)
class StaticResourceRepositoryImplTest {

    @Autowired
    private lateinit var staticResourceRepository: StaticResourceRepository

    lateinit var content: String
    lateinit var path: StaticResourcePath

    @Before
    fun setup() {
        content = "test"
        val fileName = "teststatic"
        path = StaticResourcePath(fileName, StaticResourceType.USER)
    }

    @Test
    fun save_SaveStaticResource_ShouldSaveNewFile() {
        val staticResource = StaticResource(path, content.toByteArray())

        val newResource = staticResourceRepository.save(staticResource)

        assertTrue(newResource.staticResourcePath.getAbsolutePath().toFile().exists())
    }

    @Test(expected = NullPointerException::class)
    fun save_SaveNullResource_ShouldThrowException() {
        staticResourceRepository.save(null)
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