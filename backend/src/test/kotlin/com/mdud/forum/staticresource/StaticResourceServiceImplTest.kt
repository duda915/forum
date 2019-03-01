package com.mdud.forum.staticresource

import com.mdud.forum.configuration.Variables
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.Mockito.*
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner::class)
class StaticResourceServiceImplTest {


    @InjectMocks
    lateinit var staticResourceServiceImpl: StaticResourceServiceImpl

    @Mock
    lateinit var staticResourceRepository: StaticResourceRepository

    @Test
    fun addStaticResource() {
        val bytes = "test".toByteArray()
        Mockito.`when`(staticResourceRepository.save(ArgumentMatchers.any(StaticResource::class.java))).then { it.getArgument(0) }

        val link = staticResourceServiceImpl.addStaticResource(StaticResourceType.USER, bytes)

        assertThat(link.resourceLink, CoreMatchers.containsString("${Variables.staticEndpoint}/user"))
        verify(staticResourceRepository, times(1)).save(ArgumentMatchers.any(StaticResource::class.java))
    }

    @Test
    fun removeStaticResource() {

    }
}