package klib.data.type.collections

public fun CharArray.rangeEquals(
    offset: Int,
    other: CharArray,
    otherOffset: Int,
    byteCount: Int,
): Boolean = rangeEquals(::get, offset, other::get, otherOffset, byteCount)

public fun CharArray.startsWith(prefix: CharArray): Boolean =
    rangeEquals(0, prefix, 0, prefix.size)

public fun CharArray.endsWith(suffix: CharArray): Boolean =
    rangeEquals(size - suffix.size, suffix, 0, suffix.size)
