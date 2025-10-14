package klib.data.type.collections

public fun zip(arrays: List<ShortArray>): ShortArray =
    ShortArray(arrays[0].size * arrays.size) {
        arrays[it % arrays.size][it / arrays.size]
    }

public fun ShortArray.unzip(size: Int): List<ShortArray> =
    List(size) { offset ->
        ShortArray(this.size / size) {
            this[offset + it * size]
        }
    }

public fun ShortArray.rangeEquals(
    offset: Int,
    other: ShortArray,
    otherOffset: Int,
    byteCount: Int,
): Boolean = rangeEquals(::get, offset, other::get, otherOffset, byteCount)

public fun ShortArray.startsWith(prefix: ShortArray): Boolean =
    rangeEquals(0, prefix, 0, prefix.size)

public fun ShortArray.endsWith(suffix: ShortArray): Boolean =
    rangeEquals(size - suffix.size, suffix, 0, suffix.size)
