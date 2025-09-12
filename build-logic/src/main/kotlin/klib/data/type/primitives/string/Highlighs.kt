package klib.data.type.primitives.string

import com.github.ajalt.colormath.model.RGBInt
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.*
import klib.data.type.ansi.ansiSpan
import klib.data.type.primitives.unsigned

public fun Highlights.highlight(
    transform: (text: CharSequence, bold: Boolean, rgb: UInt?) -> String = { text, bold, rgb ->
        text.ansiSpan { if (bold) bold(); if (rgb != null) attribute(RGBInt(rgb).toAnsi16()) }
    }
): String = buildString {
    val code = getCode()
    val highlights = getHighlights()

    val boundaries = (listOf(0, code.length) + highlights.flatMap { highlight ->
        listOf(
            minOf(highlight.location.start, highlight.location.end),
            maxOf(highlight.location.start, highlight.location.end)
        )
    }).distinct().sorted()

    val opens = highlights.groupBy { it.location.start }
    val closes = highlights.groupBy { it.location.end }

    var boldCount = 0
    val colorStack = mutableListOf<UInt>()
    fun current() = (boldCount > 0) to colorStack.lastOrNull()

    for (i in 0 until boundaries.size - 1) {
        val start = boundaries[i]
        val end = boundaries[i + 1]

        closes[start]?.forEach {
            when (it) {
                is BoldHighlight -> boldCount--
                is ColorHighlight -> colorStack.remove(it.rgb.toUInt() and 0xFFFFFFu)
            }
        }

        opens[start]?.forEach {
            when (it) {
                is BoldHighlight -> boldCount++
                is ColorHighlight -> colorStack.add(it.rgb.toUInt() and 0xFFFFFFu)
            }
        }

        if (end > start) {
            val (bold, rgb) = current()
            append(transform(code.subSequence(start, end), bold, rgb))
        }
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
