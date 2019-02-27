package com.mdud.forum.initializer

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class InitializerCore : CommandLineRunner {
    companion object {
        val initializerList = mutableListOf<Initializer>()
    }

    override fun run(vararg args: String?) {
        initializerList.forEach { it.initialize() }
    }
}