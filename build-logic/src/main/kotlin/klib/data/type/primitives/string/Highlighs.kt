package klib.data.type.primitives.string

import com.github.ajalt.colormath.model.RGBInt
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.BoldHighlight
import dev.snipme.highlights.model.ColorHighlight
import dev.snipme.highlights.model.SyntaxLanguage
import dev.snipme.highlights.model.SyntaxTheme
import dev.snipme.highlights.model.SyntaxThemes
import klib.data.type.ansi.ansiSpan

public fun Highlights.highlight(
    transform: (text: CharSequence, bold: Boolean, rgb: UInt?) -> String = { text, bold, rgb ->
        text.ansiSpan {
            if (bold) bold()
            if (rgb != null) attribute(RGBInt(rgb).toAnsi16())
        }
    }
): String = buildString {
    val code = getCode()
    val highlights = getHighlights()

    val boundaries = buildSet(highlights.size * 2 + 2) {
        add(0)
        add(code.length)
        highlights.forEach { highlight ->
            add(highlight.location.start)
            add(highlight.location.end)
        }
    }.sorted()

    val opens = highlights.groupBy { it.location.start }
    val closes = highlights.groupBy { it.location.end }

    var boldCount = 0
    val colorStack = mutableListOf<ColorHighlight>()
    fun currentColor(): UInt? = colorStack.lastOrNull()?.rgb?.toUInt()?.and(0xFFFFFFu)

    for (i in 0 until boundaries.size - 1) {
        val start = boundaries[i]
        val end = boundaries[i + 1]

        closes[start]?.forEach { h ->
            when (h) {
                is BoldHighlight -> boldCount--
                is ColorHighlight -> colorStack.remove(h)
            }
        }

        opens[start]?.forEach { h ->
            when (h) {
                is BoldHighlight -> boldCount++
                is ColorHighlight -> colorStack.add(h)
            }
        }

        if (end > start)
            append(transform(code.subSequence(start, end), boldCount > 0, currentColor()))
    }
}

public fun String.highlight(
    language: SyntaxLanguage = SyntaxLanguage.KOTLIN,
    theme: SyntaxTheme = SyntaxThemes.default(true),
    transform: (text: CharSequence, bold: Boolean, rgb: UInt?) -> String = { text, bold, rgb ->
        text.ansiSpan {
            if (bold) bold()
            if (rgb != null) attribute(RGBInt(rgb).toAnsi16())
        }
    }
): String = Highlights.Builder()
    .code(this)
    .language(language)
    .theme(theme)
    .build()
    .highlight(transform)
