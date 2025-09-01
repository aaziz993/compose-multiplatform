package klib.data.type.reflection

import klib.data.type.primitives.string.capitalize
import org.reflections.Reflections

public val REFLECTIONS: Reflections = Reflections()

public fun String.toGetterName(): String = if (hasPrefix("get") || hasPrefix("is")) this else "get${capitalize()}"

public fun String.toSetterName(): String =
    if (hasPrefix("set")) this else "set${(if (hasPrefix("is")) removePrefix("is") else this).capitalize()}"

private fun String.hasPrefix(prefix: String): Boolean =
    startsWith(prefix) && length > prefix.length && this[prefix.length].isLetter() && this[prefix.length].isUpperCase()
