package com.mdud.forum.staticresource

import com.mdud.forum.configuration.Variables
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.lang.IllegalArgumentException

@RunWith(SpringJUnit4ClassRunner::class)
class StaticResourcePathTest {

    @Test
    fun createFromLink_InsertValidFileName_ShouldCreatePath() {
        val staticResourcePath = StaticResourcePath("test", StaticResourceType.USER)
        val staticResourceLink = StaticResourceLink(staticResourcePath)

        val created = StaticResourcePath.createFromLink(staticResourceLink)

        Assert.assertEquals(staticResourcePath, created)
    }

    @Test(expected = IllegalArgumentException::class)
    fun createFromLink_InsertInvalidFileName_ShouldThrowException() {
        val staticResourcePath = StaticResourcePath("", StaticResourceType.USER)
        val staticResourceLink = StaticResourceLink(staticResourcePath)

        StaticResourcePath.createFromLink(staticResourceLink)
    }
}