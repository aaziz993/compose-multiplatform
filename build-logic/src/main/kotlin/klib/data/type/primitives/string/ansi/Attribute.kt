package klib.data.type.primitives.string.ansi

/**
 * Display attributes, also know as
 * [SGR
 * (Select Graphic Rendition) parameters](https://en.wikipedia.org/wiki/ANSI_escape_code#SGR_(Select_Graphic_Rendition)_parameters).
 */
public enum class Attribute(public val code: Int) {
    RESET(0),
    INTENSITY_BOLD(1),
    INTENSITY_FAINT(2),
    ITALIC(3),
    UNDERLINE(4),
    BLINK_SLOW(5),
    BLINK_FAST(6),
    NEGATIVE_ON(7),
    CONCEAL_ON(8),
    STRIKETHROUGH_ON(9),
    UNDERLINE_DOUBLE(21),
    INTENSITY_BOLD_OFF(22),
    ITALIC_OFF(23),
    UNDERLINE_OFF(24),
    BLINK_OFF(25),
    NEGATIVE_OFF(27),
    CONCEAL_OFF(28),
    STRIKETHROUGH_OFF(29);
}
