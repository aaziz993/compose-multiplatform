package klib.data.type.ansi

import klib.data.type.ansi.Color.DEFAULT
import klib.data.type.colors.Colors
import kotlinx.serialization.Serializable
import kotlin.text.removePrefix
import kotlin.text.startsWith

public val ANSI_COLOR_PREFIXES: List<String> = listOf("FG_", "BG_")
public val ANSI_BRIGHT_PREFIXES: List<String> = listOf("BRIGHT_", "LIGHT_", "HI_")

@Serializable
public data class AnsiColor(
    public val color: Color,
    public val background: Boolean = false,
    public val bright: Boolean = false,
) : HasIndex {

    override val index: Int
        get() = if (background) color.bg(bright) else color.fg(bright)

    public companion object {

        public operator fun invoke(value: String): AnsiColor =
            parseOrNull(value) ?: throw IllegalArgumentException("Unresolved ansi color '$value'")

        public fun parseOrNull(value: String): AnsiColor? {
            var cleaned = value
            var background = false
            var bright = false

            ANSI_COLOR_PREFIXES.find(cleaned::startsWith)?.let { prefix ->
                background = prefix == "BG_"
                cleaned = cleaned.removePrefix(prefix)
            }

            ANSI_BRIGHT_PREFIXES.find(cleaned::startsWith)?.let { prefix ->
                bright = true
                cleaned = cleaned.removePrefix(prefix)
            }

            val color = runCatching { Color.valueOf(cleaned) }.getOrNull() ?: return null

            return AnsiColor(color, background, bright && color != DEFAULT)
        }
    }
}
