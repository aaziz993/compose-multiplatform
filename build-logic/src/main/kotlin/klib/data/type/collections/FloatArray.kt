package klib.data.type.collections

public fun zip(arrays: List<FloatArray>): FloatArray =
    FloatArray(arrays[0].size * arrays.size) {
        arrays[it % arrays.size][it / arrays.size]
    }

public fun FloatArray.unzip(size: Int): List<FloatArray> =
    List(size) { offset ->
        FloatArray(this.size / size) {
            this[offset + it * size]
        }
    }

public fun FloatArray.rangeEquals(
    offset: Int,
    other: FloatArray,
    otherOffset: Int,
    byteCount: Int,
): Boolean = rangeEquals(::get, offset, other::get, otherOffset, byteCount)

public fun FloatArray.startsWith(prefix: FloatArray): Boolean =
    rangeEquals(0, prefix, 0, prefix.size)

public fun FloatArray.endsWith(suffix: FloatArray): Boolean =
    rangeEquals(size - suffix.size, suffix, 0, suffix.size)
