package klib.data.type

public object Ansi {
    // Reset
    public const val RESET: String = "\u001B[0m"

    // Regular Colors
    public const val BLACK: String = "\u001B[30m"
    public const val RED: String = "\u001B[31m"
    public const val GREEN: String = "\u001B[32m"
    public const val YELLOW: String = "\u001B[33m"
    public const val BLUE: String = "\u001B[34m"
    public const val PURPLE: String = "\u001B[35m"
    public const val CYAN: String = "\u001B[36m"
    public const val WHITE: String = "\u001B[37m"

    // Bright Colors
    public const val BRIGHT_BLACK: String = "\u001B[90m"
    public const val BRIGHT_RED: String = "\u001B[91m"
    public const val BRIGHT_GREEN: String = "\u001B[92m"
    public const val BRIGHT_YELLOW: String = "\u001B[93m"
    public const val BRIGHT_BLUE: String = "\u001B[94m"
    public const val BRIGHT_PURPLE: String = "\u001B[95m"
    public const val BRIGHT_CYAN: String = "\u001B[96m"
    public const val BRIGHT_WHITE: String = "\u001B[97m"

    // Background Colors
    public const val BG_BLACK: String = "\u001B[40m"
    public const val BG_RED: String = "\u001B[41m"
    public const val BG_GREEN: String = "\u001B[42m"
    public const val BG_YELLOW: String = "\u001B[43m"
    public const val BG_BLUE: String = "\u001B[44m"
    public const val BG_PURPLE: String = "\u001B[45m"
    public const val BG_CYAN: String = "\u001B[46m"
    public const val BG_WHITE: String = "\u001B[47m"

    // Bright Background Colors
    public const val BG_BRIGHT_BLACK: String = "\u001B[100m"
    public const val BG_BRIGHT_RED: String = "\u001B[101m"
    public const val BG_BRIGHT_GREEN: String = "\u001B[102m"
    public const val BG_BRIGHT_YELLOW: String = "\u001B[103m"
    public const val BG_BRIGHT_BLUE: String = "\u001B[104m"
    public const val BG_BRIGHT_PURPLE: String = "\u001B[105m"
    public const val BG_BRIGHT_CYAN: String = "\u001B[106m"
    public const val BG_BRIGHT_WHITE: String = "\u001B[107m"

    // Styles
    public const val BOLD: String = "\u001B[1m"
    public const val DIM: String = "\u001B[2m"
    public const val ITALIC: String = "\u001B[3m"
    public const val UNDERLINE: String = "\u001B[4m"
    public const val BLINK: String = "\u001B[5m"
    public const val REVERSE: String = "\u001B[7m"
    public const val HIDDEN: String = "\u001B[8m"
}

public fun String.toAnsi(ansi: Ansi): String = "$ansi$this${Ansi.RESET}"