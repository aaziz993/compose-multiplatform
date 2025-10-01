@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.fs

import klib.data.type.collections.checkOffsetAndCount
import klib.data.type.primitives.minOf
import kotlinx.io.Buffer
import kotlinx.io.Segment
import kotlinx.io.seek
import kotlinx.io.bytestring.ByteString

/**
 * Returns true if the range within this buffer starting at `segmentPos` in `segment` is equal to
 * `bytes[bytesOffset..bytesLimit)`.
 */
public fun rangeEquals(
    segment: Segment,
    segmentPos: Int,
    bytes: ByteString,
    bytesOffset: Int,
    bytesLimit: Int,
): Boolean {
    var segment = segment
    var segmentPos = segmentPos
    var segmentLimit = segment.limit
    var data = segment.dataAsByteArray()

    var i = bytesOffset
    while (i < bytesLimit) {
        if (segmentPos == segmentLimit) {
            segment = segment.next!!
            data = segment.dataAsByteArray()
            segmentPos = segment.pos
            segmentLimit = segment.limit
        }

        if (data[segmentPos] != bytes[i]) {
            return false
        }

        segmentPos++
        i++
    }

    return true
}

public fun Buffer.rangeEquals(
    offset: Long,
    bytes: ByteString,
    bytesOffset: Int = 0,
    byteCount: Int = bytes.size,
): Boolean {
    if (byteCount < 0) return false
    if (offset < 0 || offset + byteCount > size) return false
    if (bytesOffset < 0 || bytesOffset + byteCount > bytes.size) return false
    if (byteCount == 0) return true

    return indexOf(
        bytes,
        offset,
        offset + 1,
        bytesOffset,
        byteCount,
    ) != -1L
}


public fun Buffer.indexOf(
    bytes: ByteString,
    startIndex: Long,
    endIndex: Long = Long.MAX_VALUE,
    bytesOffset: Int = 0,
    byteCount: Int = bytes.size,
): Long {
    checkOffsetAndCount(bytes.size.toLong(), bytesOffset.toLong(), byteCount.toLong())
    require(byteCount > 0) { "byteCount == 0" }
    require(startIndex >= 0) { "fromIndex < 0: $startIndex" }
    require(startIndex <= endIndex) { "fromIndex > toIndex: $startIndex > $endIndex" }

    var fromIndex = startIndex
    var toIndex = endIndex
    if (toIndex > size) toIndex = size
    if (fromIndex == toIndex) return -1L

    seek(fromIndex) { s, offset ->
        var s = s ?: return -1L
        var offset = offset

        // Scan through the segments, searching for the lead byte. Each time that is found, delegate
        // to rangeEquals() to check for a complete match.
        val b0 = bytes[bytesOffset]
        val resultLimit = minOf(toIndex, size - byteCount + 1L)
        while (offset < resultLimit) {
            // Scan through the current segment.
            val data = s.dataAsByteArray()
            val segmentLimit = minOf(s.limit, s.pos + resultLimit - offset).toInt()
            for (pos in (s.pos + fromIndex - offset).toInt() until segmentLimit) {
                if (
                    data[pos] == b0 &&
                    rangeEquals(s, pos + 1, bytes, bytesOffset + 1, byteCount)
                ) {
                    return pos - s.pos + offset
                }
            }

            // Not in this segment. Try the next one.
            offset += (s.limit - s.pos).toLong()
            fromIndex = offset
            s = s.next!!
        }

        return -1L
    }
}
