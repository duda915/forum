package com.mdud.forum.integration

import com.mdud.forum.TestConfiguration
import org.springframework.test.annotation.ProfileValueSource

class IntegrationProfileValueSource : ProfileValueSource {
    override fun get(p0: String): String? {
        return when(p0) {
            "integration" -> TestConfiguration.integrationTests.toString()
            else -> "null"
        }
    }

}