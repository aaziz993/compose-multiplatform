package klib.data.type.ansi;

/**
 * [ANSI 8 colors](https://en.wikipedia.org/wiki/ANSI_escape_code#Colors) for fluent API
 */
public enum class Color(public val value: Int) {
    BLACK(0),
    RED(1),
    GREEN(2),
    YELLOW(3),
    BLUE(4),
    MAGENTA(5),
    CYAN(6),
    WHITE(7),
    DEFAULT(9);

    public fun ansi(background: Boolean = false, bright: Boolean = false): AnsiColor =
        AnsiColor(this, background, bright && this != DEFAULT)
}