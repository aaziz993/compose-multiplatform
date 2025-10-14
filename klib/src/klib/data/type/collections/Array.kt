package klib.data.type.collections


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
