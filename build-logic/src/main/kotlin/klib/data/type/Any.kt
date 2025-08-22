package klib.data.type

public inline fun <reified T> Any.cast(): T {
    return this as T
}

public val Any.asInt: Int
    get() = when (this) {
        is Int -> this

        is String -> toInt()

        else -> throw IllegalArgumentException("Expected integer value, but got $this")
    }