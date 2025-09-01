package klib.data.type

import kotlin.text.toInt

public fun Any?.hashCode(): Int = this?.hashCode() ?: 0

public fun hashCode(vararg values: Any?, fromIndex: Int = 0, length: Int = values.size, initialValue: Int = 1): Int {
    var result = initialValue
    val end = fromIndex + length
    for (i in fromIndex..<end) {
        result = 31 * result + values[i].hashCode()
    }
    return result
}

public inline fun <reified T> Any.cast(): T {
    return this as T
}