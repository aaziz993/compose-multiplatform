package klib.data.fs

import klib.data.type.collections.rangeEquals
import kotlinx.io.Buffer
import kotlinx.io.bytestring.ByteString

public fun ByteString.iterator(): Iterator<Byte> = ByteStringIterator(this)

private class ByteStringIterator(private val byteString: ByteString) : Iterator<Byte> {
    private var index: Int = 0

    override fun hasNext(): Boolean = index < byteString.size

    override fun next(): Byte = byteString[index++]
}


public fun ByteString.toAsciiLowercase(): ByteString {
    // Search for an uppercase character. If we don't find one, return this.
    var i = 0
    while (i < size) {
        var c = this[i]
        if (c < 'A'.code.toByte() || c > 'Z'.code.toByte()) {
            i++
            continue
        }

        // This string is needs to be lowercased. Create and return a new byte string.
        val lowercase = toByteArray()
        lowercase[i++] = (c - ('A' - 'a')).toByte()
        while (i < lowercase.size) {
            c = lowercase[i]
            if (c < 'A'.code.toByte() || c > 'Z'.code.toByte()) {
                i++
                continue
            }
            lowercase[i] = (c - ('A' - 'a')).toByte()
            i++
        }
        return ByteString(lowercase)
    }
    return this
}


public fun ByteString.toAsciiUppercase(): ByteString {
    // Search for an lowercase character. If we don't find one, return this.
    var i = 0
    while (i < size) {
        var c = this[i]
        if (c < 'a'.code.toByte() || c > 'z'.code.toByte()) {
            i++
            continue
        }

        // This string is needs to be uppercased. Create and return a new byte string.
        val lowercase = toByteArray()
        lowercase[i++] = (c - ('a' - 'A')).toByte()
        while (i < lowercase.size) {
            c = lowercase[i]
            if (c < 'a'.code.toByte() || c > 'z'.code.toByte()) {
                i++
                continue
            }
            lowercase[i] = (c - ('a' - 'A')).toByte()
            i++
        }
        return ByteString(lowercase)
    }
    return this
}

public fun ByteString.rangeEquals(
    offset: Int,
    other: ByteString,
    otherOffset: Int,
    byteCount: Int,
): Boolean = rangeEquals(::get, offset, other::get, otherOffset, byteCount)

public fun ByteString.rangeEquals(
    offset: Int,
    other: ByteArray,
    otherOffset: Int,
    byteCount: Int,
): Boolean = rangeEquals(::get, offset, other::get, otherOffset, byteCount)

public fun ByteString.startsWith(prefix: ByteString): Boolean =
    rangeEquals(0, prefix, 0, prefix.size)


public fun ByteString.startsWith(prefix: ByteArray): Boolean =
    rangeEquals(0, prefix, 0, prefix.size)


public fun ByteString.endsWith(suffix: ByteString): Boolean =
    rangeEquals(size - suffix.size, suffix, 0, suffix.size)


public fun ByteString.endsWith(suffix: ByteArray): Boolean =
    rangeEquals(size - suffix.size, suffix, 0, suffix.size)

public fun byteStringOf(data: ByteArray): ByteString = ByteString(data)

public fun byteStringOf(
    data: ByteArray,
    startIndex: Int = 0,
    endIndex: Int = data.size
): ByteString = ByteString(data, startIndex, endIndex)

/** Writes the contents of this byte string to `buffer`.  */
public fun ByteString.write(buffer: Buffer, offset: Int, byteCount: Int) {
    for (i in 0 until byteCount)
        buffer.writeByte(this[i + offset])
}
