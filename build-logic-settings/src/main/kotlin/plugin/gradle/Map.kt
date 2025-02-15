package plugin.gradle

import java.util.*

/**
 * Produces a new map that represents a merge of [source] into [this], returning an immutable view. Map leaves will be
 * overwritten from [source] into [this].
 *
 * @param source Nested [Map<String,Any>] that will be merged over [this]. Must be [Map<String,Any>]
 *
 * @throws IllegalArgumentException When [source] causes a [ClassCastException] caused by a non-[String] key
 */
internal fun Map<String, Any?>.deepMerge(source: Map<String, Any?>): Map<String, Any?> =
    deepMerge(Stack<String>(), source)

internal fun Map<String, Any?>.deepMerge(path: Stack<String>, source: Map<String, Any?>): Map<String, Any?> {
    return toMutableMap().let { destination ->
        try {
            for (key in source.keys) {
                if (source[key] is Map<*, *> && destination[key] is Map<*, *>) {
                    @Suppress("UNCHECKED_CAST")
                    destination[key] = (destination[key] as Map<String, Any>).deepMerge(
                        path.also { it.push(key) },
                        (source[key] as Map<String, Any>)
                    )
                } else {
                    destination[key] = source[key] as Any
                }
                path.clear()
            }
        } catch (_: ClassCastException) {
            throw IllegalArgumentException("Cannot deepMerge source['${if (path.empty()) "" else path.joinToString(".")}'] when keys are not Strings ...")
        }
        Collections.unmodifiableMap(destination)
    }
}
