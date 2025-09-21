package klib.data.type.primitives.string.ansi

/**
 * ED (Erase in Display) / EL (Erase in Line) parameter (see
 * [CSI sequence J and K](https://en.wikipedia.org/wiki/ANSI_escape_code#CSI_sequences))
 * @see Ansi.eraseScreen
 * @see Ansi.eraseLine
 */
public enum class Erase(public val value: Int) {
    FORWARD(0),
    BACKWARD(1),
    ALL(2),
}
