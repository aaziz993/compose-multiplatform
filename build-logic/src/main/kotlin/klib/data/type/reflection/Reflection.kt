package klib.data.type.reflection

import klib.data.type.primitives.string.uppercaseFirstChar
import org.reflections.Reflections

public val REFLECTIONS: Reflections = Reflections()

public fun String.toGetterName(): String = if (hasAccessPrefix("get") || hasAccessPrefix("is")) this else "get${uppercaseFirstChar()}"

public fun String.toSetterName(): String =
    if (hasAccessPrefix("set")) this else "set${(if (hasAccessPrefix("is")) removePrefix("is") else this).uppercaseFirstChar()}"

public fun String.hasAccessPrefix(prefix: String): Boolean =
    startsWith(prefix) && length > prefix.length && this[prefix.length].isLetter() && this[prefix.length].isUpperCase()
