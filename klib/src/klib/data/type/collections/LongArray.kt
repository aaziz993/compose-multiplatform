package klib.data.type.collections

public fun zip(arrays: List<LongArray>): LongArray =
    LongArray(arrays[0].size * arrays.size) {
        arrays[it % arrays.size][it / arrays.size]
    }

public fun LongArray.unzip(size: Int): List<LongArray> =
    List(size) { offset ->
        LongArray(this.size / size) {
            this[offset + it * size]
        }
    }

public fun LongArray.rangeEquals(
    offset: Int,
    other: LongArray,
    otherOffset: Int,
    byteCount: Int,
): Boolean = rangeEquals(::get, offset, other::get, otherOffset, byteCount)

public fun LongArray.startsWith(prefix: LongArray): Boolean =
    rangeEquals(0, prefix, 0, prefix.size)

public fun LongArray.endsWith(suffix: LongArray): Boolean =
    rangeEquals(size - suffix.size, suffix, 0, suffix.size)
