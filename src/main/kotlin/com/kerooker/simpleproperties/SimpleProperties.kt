package com.kerooker.simpleproperties

import com.kerooker.simpleproperties.internal.file.PropertyFileLoader
import com.kerooker.simpleproperties.internal.profile.SystemProfileLoader

/**
 * Enhanced [java.util.Properties] that reads from the classpath
 *
 * The SimpleProperties class reads profile properties defined in [filesLocation] (defaults to root). To select the profiles
 * to be loaded, you can either pass them in [profiles] (defaults to empty) or System Environment Variables/System Properties,
 * through the key `active_profiles`, for example `active_profiles=prod,mysql`.
 *
 * The profiles will be loaded in the order they're defined, so any property defined by a profile will be overriden by
 * another profile if it's loaded after it.
 *
 * This class also reads a default profile, called `application.properties`. This will be loaded first, and any other profile
 * may override its properties.
 *
 *
 * @param [profiles] The profiles you want to load. For example `listOf("prod", "mysql")`. Defaults to empty
 * @param [filesLocation] Where this should fetch properties from. Defaults to root (.)
 */
public class SimpleProperties(
    private val profiles: List<String> = emptyList(),
    private val filesLocation: String = "."
) {
    
    private val props: Map<String, String> = with(PropertyFileLoader(filesLocation)) {
            val map = loadDefaultFile().toMutableMap()
            (profiles + SystemProfileLoader().loadProfiles().distinct()).forEach { map += loadProfileFile(it) }
            map.toMap()
        }
    
    /**
     * Fetches [key] from the properties
     *
     * If the key is defined in multiple profiles, the value present in the last declared profile is returned. If the
     * key is undefined, this will throw a [PropertyNotDefinedException].
     *
     * @see [getOptional]
     * @param [key] The key to find in the properties
     * @throws [PropertyNotDefinedException] If [key] isn't defined in any profile
     */
    public operator fun get(key: String): String {
        return props[key] ?: throw PropertyNotDefinedException(key)
    }
    
    /**
     * Fetches [key] from the properties, or null if it's undefined
     *
     * Similarly to [get], this will fetch the key from the properties. If it's defined in multiple profiles, the value
     * in the last declared profile is returned. If the key is undefined, this will return null.
     *
     * @see [get]
     * @param [key] The key to find in the properties
     */
    public fun getOptional(key: String): String? {
        return props[key]
    }
    
}

/**
 * Thrown when a key is fetched from the properties, but doesn't exist
 */
public class PropertyNotDefinedException(
    missingProperty: String
) : RuntimeException("Property <$missingProperty> is not defined. Use simpleProperty.getOptional(\"$missingProperty\") instead")