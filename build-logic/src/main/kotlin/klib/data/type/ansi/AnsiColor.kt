package klib.data.type.ansi

import kotlinx.serialization.Serializable
import kotlin.text.removePrefix
import kotlin.text.startsWith

public val ANSI_COLOR_PREFIXES: List<String> = listOf("FG_", "BG_")
public val ANSI_BRIGHT_PREFIXES: List<String> = listOf("BRIGHT_", "LIGHT_", "HI_")

@Serializable
public data class AnsiColor(
    public val color: Colors,
    public val background: Boolean = false,
    public val bright: Boolean = false,
) : HasIndex {
    override val index: Int
        get() = if (background) bg(bright) else fg(bright)

    private fun fg(bright: Boolean = false): Int = color.value + (if (bright) 90 else 30)

    private fun bg(bright: Boolean = false): Int = color.value + (if (bright) 100 else 40)

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


            val color = runCatching { Colors.valueOf(cleaned) }.getOrNull() ?: return null

            return color.ansi(background, bright)
        }
    }
}
