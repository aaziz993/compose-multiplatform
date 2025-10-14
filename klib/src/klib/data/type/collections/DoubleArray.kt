package klib.data.type.collections

public fun zip(arrays: List<DoubleArray>): DoubleArray =
    DoubleArray(arrays[0].size * arrays.size) {
        arrays[it % arrays.size][it / arrays.size]
    }

public fun DoubleArray.unzip(size: Int): List<DoubleArray> =
    List(size) { offset ->
        DoubleArray(this.size / size) {
            this[offset + it * size]
        }
    }

public fun DoubleArray.rangeEquals(
    offset: Int,
    other: DoubleArray,
    otherOffset: Int,
    byteCount: Int,
): Boolean = rangeEquals(::get, offset, other::get, otherOffset, byteCount)

public fun DoubleArray.startsWith(prefix: DoubleArray): Boolean =
    rangeEquals(0, prefix, 0, prefix.size)

public fun DoubleArray.endsWith(suffix: DoubleArray): Boolean =
    rangeEquals(size - suffix.size, suffix, 0, suffix.size)
