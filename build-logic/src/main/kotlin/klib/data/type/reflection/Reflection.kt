package klib.data.type.reflection

import klib.data.type.primitives.string.uppercaseFirst
import org.reflections.Reflections

public val REFLECTIONS: Reflections = Reflections()

public fun String.toGetterName(): String = if (hasPrefix("get") || hasPrefix("is")) this else "get${uppercaseFirst()}"

public fun String.toSetterName(): String =
    if (hasPrefix("set")) this else "set${(if (hasPrefix("is")) removePrefix("is") else this).uppercaseFirst()}"

private fun String.hasPrefix(prefix: String): Boolean =
    startsWith(prefix) && length > prefix.length && this[prefix.length].isLetter() && this[prefix.length].isUpperCase()
