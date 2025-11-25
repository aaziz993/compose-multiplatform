package klib.data.type.primitives.number.decimal.model

/**
 * Enum representing the thousand separator options.
 */
public enum class ThousandSeparator(public val char: Char?) {

    NONE(null),
    COMMA(','),
    DOT('.'),
    SPACE(' '),
    APOSTROPHE('\'')
}
