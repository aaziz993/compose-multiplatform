package gradle.plugins

import org.gradle.api.plugins.ExtraPropertiesExtension

@Suppress("UNCHECKED_CAST")
public fun <T> ExtraPropertiesExtension.getOrPut(name: String, defaultValue: () -> T): T =
    if (has(name)) this[name]
    else {
        this[name] = defaultValue()
        this[name]
    } as T
