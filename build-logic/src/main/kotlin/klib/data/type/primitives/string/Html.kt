package klib.data.type.primitives.string

import kotlin.text.StringBuilder

public fun String.escapeHtml(): String = this
    .replace("&", "&amp;")
    .replace("<", "&lt;")
    .replace(">", "&gt;")
    .replace("\"", "&quot;")
    .replace("'", "&#39;")

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