package klib.data.type.serialization.properties

import kotlinx.io.Sink
import kotlinx.io.writeString

public class PropertiesWriter(
    private val sink: Sink,
    private val escapeUnicode: Boolean
) : AutoCloseable {

    public fun writeComment(value: String) {
        sink.writeString(value.toPropertyComment())
    }

    public fun writeKey(value: String) {
        sink.writeString(convert(value, true, escapeUnicode))
    }

    /** Converts unicodes to encoded &#92;uxxxx and escapes
     * special characters with a preceding slash
     */
    public fun writeValue(value: String) {
        sink.writeString("=${convert(value, false, escapeUnicode)}\n")
    }

    public fun writeKeyValue(key: String, value: String) {
        writeKey(key)
        writeValue(value)
    }

    override fun close() {
        sink.flush()
        sink.close()
    }
}

private fun String.toPropertyComment(): String = buildString {
    append("#")
    val len = this@toPropertyComment.length
    var current = 0
    var last = 0
    while (current < len) {
        val c = this@toPropertyComment[current]
        if (c > '\u00ff' || c == '\n' || c == '\r') {
            if (last != current) append(this@toPropertyComment.substring(last, current))
            if (c > '\u00ff') {
                append("\\u")
                append(c.code.toHexString(HexFormat.UpperCase))
            }
            else {
                append("\n")
                if (c == '\r' && current != len - 1 && this@toPropertyComment[current + 1] == '\n') {
                    current++
                }
                if (current == len - 1 ||
                    (this@toPropertyComment[current + 1] != '#' &&
                        this@toPropertyComment[current + 1] != '!')
                ) append("#")
            }
            last = current + 1
        }
        current++
    }
    if (last != current) append(this@toPropertyComment.substring(last, current))
    append("\n")
}

private fun convert(
    theString: String,
    escapeSpace: Boolean,
    escapeUnicode: Boolean
): String {

    val len = theString.length
    var bufLen = len * 2
    if (bufLen < 0) {
        bufLen = Int.Companion.MAX_VALUE
    }

    return buildString(bufLen) {
        for (x in 0..<len) {
            val aChar = theString[x]
            // Handle common case first, selecting the largest block that
            // avoids the specials below
            if ((aChar.code > 61) && (aChar.code < 127)) {
                if (aChar == '\\') {
                    append('\\')
                    append('\\')
                    continue
                }
                append(aChar)
                continue
            }
            when (aChar) {
                ' ' -> {
                    if (x == 0 || escapeSpace) append('\\')
                    append(' ')
                }

                '\t' -> {
                    append('\\')
                    append('t')
                }

                '\n' -> {
                    append('\\')
                    append('n')
                }

                '\r' -> {
                    append('\\')
                    append('r')
                }

                '\u000c' -> {
                    append('\\')
                    append('f')
                }

                '=', ':', '#', '!' -> {
                    append('\\')
                    append(aChar)
                }

                else -> if (((aChar.code < 0x0020) || (aChar.code > 0x007e)) and escapeUnicode) {
                    append("\\u")
                    append(aChar.code.toHexString(HexFormat.UpperCase))
                }
                else {
                    append(aChar)
                }
            }
        }
    }
}
