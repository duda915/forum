package com.mdud.forum.staticresource

import com.mdud.forum.configuration.Variables
import com.mdud.forum.staticresource.util.StaticResourceLink
import com.mdud.forum.staticresource.util.StaticResourcePath
import com.mdud.forum.staticresource.util.StaticResourceType
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(
        classes = [Variables::class]
)
class StaticResourceServiceImplTest {


    @InjectMocks
    lateinit var staticResourceServiceImpl: StaticResourceServiceImpl

    @Mock
    lateinit var staticResourceRepository: StaticResourceRepository

    @Autowired
    @Spy
    lateinit var variables: Variables

    @Test
    fun addStaticResource() {
        val bytes = "test".toByteArray()
        Mockito.`when`(staticResourceRepository.save(ArgumentMatchers.any(StaticResource::class.java))).then { it.getArgument(0) }

        val link = staticResourceServiceImpl.addStaticResource(StaticResourceType.USER, bytes)

        assertThat(link.resourceLink, CoreMatchers.containsString("${variables.staticEndpoint}/user"))
        verify(staticResourceRepository, times(1)).save(ArgumentMatchers.any(StaticResource::class.java))
    }


}