package org.example.klib.data.type.primitives.string

import com.github.ajalt.colormath.model.RGBInt
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.*
import klib.data.type.ansi.ansiSpan

public val DEFAULT_MAPPER: (text: String, bold: Boolean, rgb: UInt?) -> String = { text, bold, rgb ->
    text.ansiSpan {
        if (bold) bold()
        if (rgb != null) attribute(RGBInt(rgb).toAnsi16())
    }
}

public fun Highlights.highlight(
    transform: (text: String, bold: Boolean, rgb: UInt?) -> String = DEFAULT_MAPPER
): String = buildString {
    val code = getCode()
    val removes = mutableMapOf<Int, MutableList<CodeHighlight>>()
    val adds = mutableMapOf<Int, MutableList<CodeHighlight>>()
    val bounds = mutableSetOf(0, code.length)

    for (highlight in getHighlights()) {
        val start = highlight.location.start
        val end = highlight.location.end

        bounds += start;
        bounds += end
        removes.getOrPut(end, ::mutableListOf).add(highlight)
        adds.getOrPut(start, ::mutableListOf).add(highlight)
    }

    val sorted = bounds.toList().sorted()

    // active state
    var boldCount = 0
    val colorStack = LinkedHashMap<CodeHighlight, UInt>() // last entry wins

    fun current() = (boldCount > 0) to colorStack.entries.lastOrNull()?.value

    for (i in 0 until sorted.size - 1) {
        val p = sorted[i]
        val q = sorted[i + 1]

        // close at p
        removes[p]?.forEach { h ->
            when (h) {
                is BoldHighlight -> if (boldCount > 0) boldCount--
                is ColorHighlight -> colorStack.remove(h)
            }
        }
        // open at p
        adds[p]?.forEach { h ->
            when (h) {
                is BoldHighlight -> boldCount++
                is ColorHighlight -> colorStack[h] = h.rgb.toUInt() and 0xFFFFFFu
            }
        }

        if (q > p) {
            val (bold, rgb) = current()
            append(transform(code.substring(p, q), bold, rgb))
        }
    }
}

public fun String.highlight(
    language: SyntaxLanguage = SyntaxLanguage.KOTLIN,
    theme: SyntaxTheme = SyntaxThemes.atom(),
    transform: (text: String, bold: Boolean, rgb: UInt?) -> String = DEFAULT_MAPPER
): String = Highlights.Builder()
    .code(this)
    .language(language)
    .theme(theme)
    .build()
    .highlight(transform)
