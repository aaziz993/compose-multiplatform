@file:Suppress("UNCHECKED_CAST")

package klib.data.type

public val Any?.asListOrNull: List<Any?>?
    get() = this as? List<Any?>

public val Any?.asList: List<Any?>
    get() = asListOrNull!!

public val Any?.asMapOrNull: Map<String, Any?>?
    get() = this as? Map<String, Any?>

public val Any?.asMap: Map<String, Any?>
    get() = asMapOrNull!!
