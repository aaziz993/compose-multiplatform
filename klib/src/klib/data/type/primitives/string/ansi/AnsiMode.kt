package klib.data.type.primitives.string.ansi

/**
 * Ansi mode.
 *
 * @since 2.1
 */
public enum class AnsiMode(public val description: String) {
    Strip("Strip all ansi sequences"),
    Default("Print ansi sequences if the stream is a terminal"),
    Force("Always print ansi sequences, even if the stream is redirected");
}
