package klib.data.type.primitives.string

import klib.data.type.collections.bimap.biMapOf
import kotlin.text.StringBuilder

private val HTML_ESCAPE_MAP = biMapOf(
    '&' to "&amp;",
    '<' to "&lt;",
    '>' to "&gt;",
    '"' to "&quot;",
    '\'' to "&#39;",
)

public fun String.escapeHtml(): String = buildString {
    this@escapeHtml.forEach { char ->
        append(HTML_ESCAPE_MAP[char] ?: char)
    }
}

public fun String.unescapeHtml(): String = buildString {
    var i = 0
    while (i < length) {
        if (this@unescapeHtml[i] == '&') {
            // Named entity
            val named = HTML_ESCAPE_MAP.inverse.entries.firstOrNull { (key, _) ->
                this@unescapeHtml.startsWith(key, i)
            }
            if (named != null) {
                append(named.value)
                i += named.key.length
                continue
            }

            // Numeric entity: decimal &#NNN; or hex &#xHHHH;
            if (this@unescapeHtml.startsWith("&#", i)) {
                val semi = indexOf(';', i + 2)
                if (semi != -1) {
                    val code = this@unescapeHtml.substring(i + 2, semi)
                    val char = try {
                        if (code.startsWith("x", ignoreCase = true)) code.drop(1).toInt(16).toChar()
                        else code.toInt().toChar()
                    }
                    catch (_: NumberFormatException) {
                        null
                    }
                    append(char ?: '&')
                    i = semi + 1
                    continue
                }
            }

            // Fallback for malformed entity
            append('&')
            i++
        }
        else {
            append(this@unescapeHtml[i])
            i++
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
