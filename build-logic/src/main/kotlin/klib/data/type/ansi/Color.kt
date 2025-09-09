package klib.data.type.ansi;

/**
 * [ANSI 8 colors](https://en.wikipedia.org/wiki/ANSI_escape_code#Colors) for fluent API
 */
public enum class Color(public val value: Int) : HasIndex {

    BLACK(0),
    RED(1),
    GREEN(2),
    YELLOW(3),
    BLUE(4),
    MAGENTA(5),
    CYAN(6),
    WHITE(7),
    DEFAULT(9);

    override val index: Int
        get() = fg()

    public fun fg(bright: Boolean = false): Int = value + (if (bright) 90 else 30)

    public fun bg(bright: Boolean = false): Int = value + (if (bright) 100 else 40)
}
