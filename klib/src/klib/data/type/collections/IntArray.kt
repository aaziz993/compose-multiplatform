package klib.data.type.collections

public fun zip(arrays: List<IntArray>): IntArray =
    IntArray(arrays[0].size * arrays.size) {
        arrays[it % arrays.size][it / arrays.size]
    }

public fun IntArray.unzip(size: Int): List<IntArray> =
    List(size) { offset ->
        IntArray(this.size / size) {
            this[offset + it * size]
        }
    }

public fun IntArray.rangeEquals(
    offset: Int,
    other: IntArray,
    otherOffset: Int,
    byteCount: Int,
): Boolean = rangeEquals(::get, offset, other::get, otherOffset, byteCount)

public fun IntArray.startsWith(prefix: IntArray): Boolean =
    rangeEquals(0, prefix, 0, prefix.size)

public fun IntArray.endsWith(suffix: IntArray): Boolean =
    rangeEquals(size - suffix.size, suffix, 0, suffix.size)
