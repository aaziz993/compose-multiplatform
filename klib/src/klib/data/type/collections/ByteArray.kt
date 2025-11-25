package klib.data.type.collections

import klib.data.type.primitives.number.toByteArray

public fun ByteArray.withLengthHeader(): ByteArray = size.toLong().toByteArray(Long.SIZE_BYTES) + this

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

public fun ByteArray.rangeEquals(
    offset: Int,
    other: ByteArray,
    otherOffset: Int,
    byteCount: Int,
): Boolean = rangeEquals(::get, offset, other::get, otherOffset, byteCount)

public fun ByteArray.startsWith(prefix: ByteArray): Boolean =
    rangeEquals(0, prefix, 0, prefix.size)

public fun ByteArray.endsWith(suffix: ByteArray): Boolean =
    rangeEquals(size - suffix.size, suffix, 0, suffix.size)
