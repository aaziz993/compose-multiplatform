package klib.data.type.primitives.string

import klib.data.type.collections.bimap.BiMap
import klib.data.type.collections.bimap.biMapOf

public val HTML_ESCAPE_MAP: BiMap<Char, String> = biMapOf(
    '&' to "&amp;",
    '<' to "&lt;",
    '>' to "&gt;",
    '"' to "&quot;",
    '\'' to "&#39;",
)

public fun String.escapeHtml(): String = buildString {
    for (char in this@escapeHtml) {
        append(HTML_ESCAPE_MAP[char] ?: char)
    }
}

public fun String.unescapeHtml(): String = buildString {
    val value = this@unescapeHtml
    val inverse = HTML_ESCAPE_MAP.inverse
    var index = 0

    while (index < value.length) {
        if (value[index] == '&') {
            // Named entity
            val matched = inverse.entries.firstOrNull { (key, _) -> value.startsWith(key, index) }
            if (matched != null) {
                append(matched.value)
                index += matched.key.length
                continue
            }

            // Numeric entity
            if (value.startsWith("&#", index)) {
                val semi = value.indexOf(';', index + 2)
                if (semi != -1) {
                    val code = value.substring(index + 2, semi)
                    val char = try {
                        if (code.startsWith("x", ignoreCase = true)) code.drop(1).toInt(16).toChar()
                        else code.toInt().toChar()
                    } catch (_: NumberFormatException) { null }

                    append(char ?: value.substring(index, semi + 1))
                    index = semi + 1
                    continue
                }
            }

            // Fallback
            append('&')
            index++
        } else {
            append(value[index])
            index++
        }
    }
}

public fun String.htmlSpan(
    color: String? = null,
    bold: Boolean = false,
    background: String? = null,
    block: StringBuilder.() -> Unit = {}
): String {
    val style = buildString {
        if (color != null) append("color:$color;")
        if (background != null) append("background-color:$background;")
        if (bold) append("font-weight:bold;")
    }

    val inner = buildString {
        append(this@htmlSpan)
        block()
    }

    return if (style.isNotEmpty()) "<span style=\"$style\">$inner</span>" else inner
}
