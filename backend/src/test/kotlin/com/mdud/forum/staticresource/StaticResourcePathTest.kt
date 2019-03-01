package com.mdud.forum.staticresource

import com.mdud.forum.configuration.Variables
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner::class)
class StaticResourcePathTest {

    @Test
    fun createFromString_ValidString_ShouldCreatePath() {
        val resource = "${Variables.staticEndpoint}/${StaticResourceType.IMAGE}/test"

        val created = StaticResourcePath.createFromString(resource)

        val expected = StaticResourcePath("test", StaticResourceType.IMAGE)

        Assert.assertEquals(expected, created)
    }

    @Test(expected = IllegalArgumentException::class)
    fun createFromString_CreateFromInvalidEndPoint_ShouldThrowException() {
        val resource = "invalid/${StaticResourceType.IMAGE}/test"

        StaticResourcePath.createFromString(resource)
    }

    @Test(expected = IllegalArgumentException::class)
    fun createFromString_CreateFromInvalidResourceType_ShouldThrowException() {
        val resource = "${Variables.staticEndpoint}/invalid/test"

        StaticResourcePath.createFromString(resource)
    }

    @Test(expected = IllegalArgumentException::class)
    fun createFromString_CreateFromInvalidFileName_ShouldThrowException() {
        val resource = "${Variables.staticEndpoint}/${StaticResourceType.IMAGE}/"

        StaticResourcePath.createFromString(resource)
    }

}