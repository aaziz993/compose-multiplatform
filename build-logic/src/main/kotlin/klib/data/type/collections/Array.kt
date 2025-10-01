package klib.data.type.collections

import kotlin.IndexOutOfBoundsException

public fun <E> Array<out E>.drop(): List<E> = drop(1)

public fun <E> Array<out E>.dropLast(): List<E> = dropLast(1)

public inline fun <reified E> zip(arrays: List<Array<E>>): Array<E> =
    Array(arrays[0].size * arrays.size) {
        arrays[it % arrays.size][it / arrays.size]
    }

public inline fun <reified E> Array<E>.unzip(size: Int): List<Array<E>> =
    List(size) { offset ->
        Array(this.size / size) {
            this[offset + it * size]
        }
    }

public fun zip(arrays: List<BooleanArray>): BooleanArray =
    BooleanArray(arrays[0].size * arrays.size) {
        arrays[it % arrays.size][it / arrays.size]
    }

public fun BooleanArray.unzip(size: Int): List<BooleanArray> =
    List(size) { offset ->
        BooleanArray(this.size / size) {
            this[offset + it * size]
        }
    }

public fun zip(arrays: List<ByteArray>): ByteArray =
    ByteArray(arrays[0].size * arrays.size) {
        arrays[it % arrays.size][it / arrays.size]
    }

public fun ByteArray.unzip(size: Int): List<ByteArray> =
    List(size) { offset ->
        ByteArray(this.size / size) {
            this[offset + it * size]
        }
    }

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

public fun <E> Array<E>.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

public fun checkOffsetAndCount(size: Long, offset: Long, byteCount: Long) {
    if (offset or byteCount < 0 || offset > size || size - offset < byteCount) {
        throw IndexOutOfBoundsException("size=$size offset=$offset byteCount=$byteCount")
    }
}

public fun <T> rangeEquals(
    getter: (index: Int) -> T,
    offset: Int,
    otherGetter: (index: Int) -> T,
    otherOffset: Int,
    byteCount: Int,
): Boolean {
    for (i in 0 until byteCount)
        if (getter(i + offset) != otherGetter(i + otherOffset)) return false
    return true
}

public fun <T> Array<T>.rangeEquals(
    offset: Int,
    other: Array<T>,
    otherOffset: Int,
    byteCount: Int,
): Boolean = rangeEquals(::get, offset, other::get, otherOffset, byteCount)

public fun ByteArray.rangeEquals(
    offset: Int,
    other: ByteArray,
    otherOffset: Int,
    byteCount: Int,
): Boolean = rangeEquals(::get, offset, other::get, otherOffset, byteCount)

public fun ShortArray.rangeEquals(
    offset: Int,
    other: ShortArray,
    otherOffset: Int,
    byteCount: Int,
): Boolean = rangeEquals(::get, offset, other::get, otherOffset, byteCount)

public fun IntArray.rangeEquals(
    offset: Int,
    other: IntArray,
    otherOffset: Int,
    byteCount: Int,
): Boolean = rangeEquals(::get, offset, other::get, otherOffset, byteCount)

public fun LongArray.rangeEquals(
    offset: Int,
    other: LongArray,
    otherOffset: Int,
    byteCount: Int,
): Boolean = rangeEquals(::get, offset, other::get, otherOffset, byteCount)

public fun FloatArray.rangeEquals(
    offset: Int,
    other: FloatArray,
    otherOffset: Int,
    byteCount: Int,
): Boolean = rangeEquals(::get, offset, other::get, otherOffset, byteCount)

public fun DoubleArray.rangeEquals(
    offset: Int,
    other: DoubleArray,
    otherOffset: Int,
    byteCount: Int,
): Boolean = rangeEquals(::get, offset, other::get, otherOffset, byteCount)

public fun CharArray.rangeEquals(
    offset: Int,
    other: CharArray,
    otherOffset: Int,
    byteCount: Int,
): Boolean = rangeEquals(::get, offset, other::get, otherOffset, byteCount)

