package com.mdud.forum.initializer

import javax.annotation.PostConstruct

abstract class BaseInitializer : Initializer {

    @PostConstruct
    fun registerInitializer() {
        InitializerCore.initializerList.add(this)
    }
}