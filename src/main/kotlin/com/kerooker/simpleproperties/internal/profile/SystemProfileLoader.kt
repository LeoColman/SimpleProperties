package com.kerooker.simpleproperties.internal.profile

import java.lang.System.getProperty
import java.lang.System.getenv

internal class SystemProfileLoader {
    
    fun loadProfiles(): List<String> = environmentProfiles().ifEmpty { propertyProfiles() }
    
    private fun environmentProfiles() = getenv("active_profiles").extractProfiles()
    
    private fun propertyProfiles() = getProperty("active_profiles").extractProfiles()
    
    private fun String?.extractProfiles() = this?.split(",") ?: emptyList()
}