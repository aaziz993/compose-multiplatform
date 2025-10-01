@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.fs

import kotlinx.io.Buffer
import kotlinx.io.bytestring.ByteString
import kotlinx.io.bytestring.decodeToString
import kotlinx.io.bytestring.encodeToByteString
import kotlinx.io.bytestring.indexOf
import kotlinx.io.bytestring.lastIndexOf
import kotlinx.io.files.Path
import kotlinx.io.files.SystemPathSeparator
import kotlinx.io.indexOf
import kotlinx.io.readByteString
import kotlinx.io.write
import kotlinx.io.writeString

public val Path.nameBytes: ByteString
    get() = name.encodeToByteString()

public val Path.segmentsBytes: List<ByteString>
    get() = segmentsBytes()

public val Path.segments: List<String>
    get() = segments()

public val Path.root: Path?
    get() = root()

public val Path.isRoot: Boolean
    get() = isRoot()
public val Path.volumeLetter: Char?
    get() = volumeLetter()

public fun pathOrNull(base: String, vararg parts: String): Path? =
    try {
        Path(base, *parts)
    } catch (_: IllegalArgumentException) {
        null
    }

private val SLASH = "/".encodeToByteString()

private val BACKSLASH = "\\".encodeToByteString()

private val ANY_SLASH = "/\\".encodeToByteString()

private val DOT = ".".encodeToByteString()

private val DOT_DOT = "..".encodeToByteString()

private val Path.bytes
    get() = toString().encodeToByteString()

private fun Path(byteString: ByteString) = Path(byteString.decodeToString())

internal fun Path.root(): Path? =
    when (val rootLength = rootLength()) {
        -1 -> null
        else -> Path(bytes.substring(0, rootLength))
    }

internal fun Path.segments(): List<String> = segmentsBytes().map(ByteString::decodeToString)

/** This function skips the root then splits on slash. */

internal fun Path.segmentsBytes(): List<ByteString> {
    val result = mutableListOf<ByteString>()
    var segmentStart = rootLength()

    // segmentStart should always follow a `\`, but for UNC paths it doesn't.
    if (segmentStart == -1) {
        segmentStart = 0
    } else if (segmentStart < bytes.size && bytes[segmentStart] == '\\'.code.toByte()) {
        segmentStart++
    }

    for (i in segmentStart until bytes.size) {
        if (bytes[i] == '/'.code.toByte() || bytes[i] == '\\'.code.toByte()) {
            result += bytes.substring(segmentStart, i)
            segmentStart = i + 1
        }
    }

    if (segmentStart < bytes.size) {
        result += bytes.substring(segmentStart, bytes.size)
    }

    return result
}

/** Return the length of the prefix of this that is the root path, or -1 if it has no root. */
private fun Path.rootLength(): Int {
    if (bytes.size == 0) return -1
    if (bytes[0] == '/'.code.toByte()) return 1

    if (bytes[0] == '\\'.code.toByte()) {
        if (bytes.size > 2 && bytes[1] == '\\'.code.toByte()) {
            // Look for a root like `\\localhost`.
            var uncRootEnd = bytes.indexOf(BACKSLASH, startIndex = 2)
            if (uncRootEnd == -1) uncRootEnd = bytes.size
            return uncRootEnd
        }

        // We found a root like `\`.
        return 1
    }

    // Look for a root like `C:\`.
    if (bytes.size > 2 && bytes[1] == ':'.code.toByte() && bytes[2] == '\\'.code.toByte()) {
        val c = bytes[0].toInt().toChar()
        if (c !in 'a'..'z' && c !in 'A'..'Z') return -1
        return 3
    }

    return -1
}

public val Path.commonIsRelative: Boolean
    get() = !isAbsolute

internal fun Path.volumeLetter(): Char? {
    if (bytes.indexOf(SLASH) != -1) return null
    if (bytes.size < 2) return null
    if (bytes[1] != ':'.code.toByte()) return null
    val c = bytes[0].toInt().toChar()
    if (c !in 'a'..'z' && c !in 'A'..'Z') return null
    return c
}

private val Path.indexOfLastSlash: Int
    get() {
        val lastSlash = bytes.lastIndexOf(SLASH)
        if (lastSlash != -1) return lastSlash
        return bytes.lastIndexOf(BACKSLASH)
    }

public fun Path.nameBytes(): ByteString {
    val lastSlash = indexOfLastSlash
    return when {
        lastSlash != -1 -> bytes.substring(lastSlash + 1)
        volumeLetter != null && bytes.size == 2 -> ByteString.EMPTY // "C:" has no name.
        else -> bytes
    }
}

internal fun Path.isRoot(): Boolean = rootLength() == bytes.size

public fun Path.resolve(child: String, normalize: Boolean): Path =
    resolve(Buffer().apply { writeString(child) }, normalize = normalize)

public fun Path.resolve(child: ByteString, normalize: Boolean): Path =
    resolve(Buffer().apply { write(child) }, normalize = normalize)

public fun Path.resolve(child: Buffer, normalize: Boolean): Path =
    resolve(child.toPath(normalize = false), normalize = normalize)

public fun Path.resolve(child: Path, normalize: Boolean): Path {
    if (child.isAbsolute || child.volumeLetter != null) return child

    val slash = slash ?: child.slash ?: SystemPathSeparator.toSlash()

    val buffer = Buffer()
    buffer.write(bytes)
    if (buffer.size > 0) {
        buffer.write(slash)
    }
    buffer.write(child.bytes)
    return buffer.toPath(normalize = normalize)
}

public fun Path.relativeTo(other: Path): Path {
    require(root == other.root) {
        "Paths of different roots cannot be relative to each other: $this and $other"
    }

    val thisSegments = this.segmentsBytes
    val otherSegments = other.segmentsBytes

    // We look at the path both have in common.
    var firstNewSegmentIndex = 0
    val minSegmentsSize = minOf(thisSegments.size, otherSegments.size)
    while (firstNewSegmentIndex < minSegmentsSize &&
        thisSegments[firstNewSegmentIndex] == otherSegments[firstNewSegmentIndex]
    ) {
        firstNewSegmentIndex++
    }

    if (firstNewSegmentIndex == minSegmentsSize && bytes.size == other.bytes.size) {
        // `this` and `other` are the same path.
        return ".".toPath()
    }

    require(
        otherSegments.subList(firstNewSegmentIndex, otherSegments.size).indexOf(DOT_DOT) == -1
    ) {
        "Impossible relative path to resolve: $this and $other"
    }

    if (other.bytes == DOT) {
        // Anything relative to "." is itself!
        return this
    }

    val buffer = Buffer()
    val slash = other.slash ?: slash ?: SystemPathSeparator.toSlash()
    for (i in firstNewSegmentIndex until otherSegments.size) {
        buffer.write(DOT_DOT)
        buffer.write(slash)
    }
    for (i in firstNewSegmentIndex until thisSegments.size) {
        buffer.write(thisSegments[i])
        buffer.write(slash)
    }
    return buffer.toPath(normalize = false)
}


public fun Path.normalized(): Path = toString().toPath(normalize = true)

private val Path.slash: ByteString?
    get() {
        return when {
            bytes.indexOf(SLASH) != -1 -> SLASH
            bytes.indexOf(BACKSLASH) != -1 -> BACKSLASH
            else -> null
        }
    }

public fun Path.compareTo(other: Path): Int = toString().compareTo(other.toString())

public fun String.toPath(normalize: Boolean = false): Path =
    Buffer().apply { writeString(this@toPath) }.toPath(normalize)

/** Consume the buffer and return it as a path. */
public fun Buffer.toPath(normalize: Boolean = false): Path {
    var slash: ByteString? = null
    val result = Buffer()

    // Consume the absolute path prefix, like `/`, `\\`, `C:`, or `C:\` and write the
    // canonicalized prefix to result.
    var leadingSlashCount = 0
    while (rangeEquals(0L, SLASH) || rangeEquals(0L, BACKSLASH)) {
        val byte = readByte()
        slash = slash ?: byte.toSlash()
        leadingSlashCount++
    }
    val windowsUncPath = leadingSlashCount >= 2 && slash == BACKSLASH
    if (windowsUncPath) {
        // This is a Windows UNC path, like \\server\directory\file.txt.
        result.write(slash!!)
        result.write(slash)
    } else if (leadingSlashCount > 0) {
        // This is platform-dependent:
        //  * On UNIX: an absolute path like /home
        //  * On Windows: this is relative to the current volume, like \Windows.
        result.write(slash!!)
    } else {
        // This path doesn't start with any slash. We must initialize the slash character to use.
        val limit = indexOf(ANY_SLASH)
        slash = slash ?: when (limit) {
            -1L -> SystemPathSeparator.toSlash()
            else -> get(limit).toSlash()
        }
        if (startsWithVolumeLetterAndColon(slash)) {
            if (limit == 2L) {
                result.write(this, 3L) // Absolute on a named volume, like `C:\`.
            } else {
                result.write(this, 2L) // Relative to the named volume, like `C:`.
            }
        }
    }

    val absolute = result.size > 0

    val canonicalParts = mutableListOf<ByteString>()
    while (!exhausted()) {
        val limit = indexOf(ANY_SLASH)

        val part: ByteString
        if (limit == -1L) {
            part = readByteString()
        } else {
            part = readByteString(limit.toInt())
            readByte()
        }

        if (part == DOT_DOT) {
            if (absolute && canonicalParts.isEmpty()) {
                // Silently consume `..`.
            } else if (!normalize || !absolute && (canonicalParts.isEmpty() || canonicalParts.last() == DOT_DOT)) {
                canonicalParts.add(part) // '..' doesn't pop '..' for relative paths.
            } else if (windowsUncPath && canonicalParts.size == 1) {
                // `..` doesn't pop UNC hostnames.
            } else {
                canonicalParts.removeLastOrNull()
            }
        } else if (part != DOT && part != ByteString.EMPTY) {
            canonicalParts.add(part)
        }
    }

    for (i in 0 until canonicalParts.size) {
        if (i > 0) result.write(slash)
        result.write(canonicalParts[i])
    }
    if (result.size == 0L) {
        result.write(DOT)
    }

    return Path(result.readByteString())
}

private fun Char.toSlash(): ByteString {
    return when (this) {
        '/' -> SLASH
        '\\' -> BACKSLASH
        else -> throw IllegalArgumentException("not a directory separator: $this")
    }
}

private fun Byte.toSlash(): ByteString {
    return when (toInt()) {
        '/'.code -> SLASH
        '\\'.code -> BACKSLASH
        else -> throw IllegalArgumentException("not a directory separator: $this")
    }
}

private fun Buffer.startsWithVolumeLetterAndColon(slash: ByteString): Boolean {
    if (slash != BACKSLASH) return false
    if (size < 2) return false
    if (get(1) != ':'.code.toByte()) return false
    val b = get(0).toInt().toChar()
    return b in 'a'..'z' || b in 'A'..'Z'
}
