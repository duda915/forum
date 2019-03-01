package com.mdud.forum.staticresource

import com.mdud.forum.configuration.Variables
import com.mdud.forum.staticresource.util.StaticResourceType
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner::class)
class StaticResourceServiceImplTest {


    @InjectMocks
    lateinit var staticResourceServiceImpl: StaticResourceServiceImpl

    @Mock
    lateinit var staticResourceRepository: StaticResourceRepository

    @Mock
    lateinit var variables: Variables

    @Test
    fun addStaticResource() {
        val bytes = "test".toByteArray()
        Mockito.`when`(staticResourceRepository.save(ArgumentMatchers.any())).then { it.getArgument(0) }

        val resource = staticResourceServiceImpl.addStaticResource(StaticResourceType.USER, bytes)

        assertEquals(bytes, resource.content)
        verify(staticResourceRepository, times(1)).save(resource)
    }


}