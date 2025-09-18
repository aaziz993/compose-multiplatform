package klib.data.type.reflection

import klib.data.type.primitives.string.uppercaseFirstChar
import org.reflections.Reflections

public val REFLECTIONS: Reflections = Reflections()

public fun String.toGetterName(): String = if (hasPrefix("get") || hasPrefix("is")) this else "get${uppercaseFirstChar()}"

public fun String.toSetterName(): String =
    if (hasPrefix("set")) this else "set${(if (hasPrefix("is")) removePrefix("is") else this).uppercaseFirstChar()}"

private fun String.hasPrefix(prefix: String): Boolean =
    startsWith(prefix) && length > prefix.length && this[prefix.length].isLetter() && this[prefix.length].isUpperCase()
