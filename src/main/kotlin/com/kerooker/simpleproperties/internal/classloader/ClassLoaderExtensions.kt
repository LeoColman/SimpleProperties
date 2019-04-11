package com.kerooker.simpleproperties.internal.classloader

internal val <T : Any> T.classLoader
    get() = this::class.java.classLoader