package klib.data.type.reflection

import klib.data.type.primitives.string.capitalized

public val String.asGetterName: String
    get() = if (hasPrefix("get") || hasPrefix("is")) this else "get$capitalized"

public val String.asSetterName: String
    get() = if (hasPrefix("set")) this
    else "set${(if (hasPrefix("is")) removePrefix("is") else this).capitalized}"

private fun String.hasPrefix(prefix: String): Boolean =
    startsWith(prefix) && length > prefix.length && this[prefix.length].isLetter() && this[prefix.length].isUpperCase()
