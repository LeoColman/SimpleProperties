package com.kerooker.simpleproperties.internal.file

import com.kerooker.simpleproperties.internal.classloader.classLoader
import java.io.InputStream
import java.util.Properties

internal class PropertyFileLoader(
    private val filesLocation: String = "."
) {
    
    fun loadDefaultFile(): Map<String, String> {
        return try {
            mapPropertyFile("application.properties")
        } catch (_: Throwable) {
            emptyMap()
        }
    }
    
    fun loadProfileFile(profile: String): Map<String, String> {
        return try {
            mapPropertyFile("application-$profile.properties")
        } catch(_: Throwable) {
            throw FailedToLoadProfileException(profile, filesLocation)
        }
    }
    
    private fun mapPropertyFile(fileName: String) = streamFileFromClasspath("$filesLocation/$fileName").toMap()
    
    private fun streamFileFromClasspath(fileName: String) = classLoader.getResourceAsStream(fileName)
    
    @Suppress("UNCHECKED_CAST")
    private fun InputStream.toMap(): Map<String, String> {
        val props = Properties()
        props.load(this)
        return props.toMap() as Map<String, String>
    }
}

internal class FailedToLoadProfileException(
    profile: String, location: String
) : RuntimeException("Could not load profile <$profile> from <$location>")
