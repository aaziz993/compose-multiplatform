package klib.data.type.primitives.string

import com.github.ajalt.colormath.model.RGBInt
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.*
import klib.data.type.ansi.ansiSpan
import klib.data.type.primitives.unsigned

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
    val highlights = getHighlights().sortedWith(compareBy({ it.location.start }, { it.location.end }))

    // Collect all unique boundaries (start/end positions of highlights)
    val boundaries = mutableSetOf(0, code.length)
    highlights.forEach { highlight ->
        val (start, end) = highlight.location.let { (s, e) -> if (s < e) s to e else e to s }
        boundaries += start
        boundaries += end
    }

    val sortedBounds = boundaries.sorted()

    // Active state for overlapping highlights
    var boldCount = 0
    val colorStack = LinkedHashMap<CodeHighlight, UInt>() // last color wins

    fun current(): Pair<Boolean, UInt?> = (boldCount > 0) to colorStack.entries.lastOrNull()?.value

    // Map for quick access to highlights starting/ending at a position
    val opens = highlights.groupBy { it.location.start }
    val closes = highlights.groupBy { it.location.end }

    for (i in 0 until sortedBounds.size - 1) {
        val start = sortedBounds[i]
        val end = sortedBounds[i + 1]

        // Close highlights that end at this boundary
        closes[start]?.forEach { h ->
            when (h) {
                is BoldHighlight -> if (boldCount > 0) boldCount--
                is ColorHighlight -> colorStack.remove(h)
            }
        }

        // Open highlights that start at this boundary
        opens[start]?.forEach { h ->
            when (h) {
                is BoldHighlight -> boldCount++
                is ColorHighlight -> colorStack[h] = h.rgb.toUInt() and 0xFFFFFFu
            }
        }

        // Append the substring with current active highlights
        if (end > start) {
            val (bold, rgb) = current()
            append(transform(code.substring(start, end), bold, rgb))
        }
    }
}

public fun String.highlight(
    language: SyntaxLanguage = SyntaxLanguage.KOTLIN,
    theme: SyntaxTheme = SyntaxThemes.default(true),
    transform: (text: String, bold: Boolean, rgb: UInt?) -> String = DEFAULT_MAPPER
): String = Highlights.Builder()
    .code(this)
    .language(language)
    .theme(theme)
    .build()
    .highlight(transform)
