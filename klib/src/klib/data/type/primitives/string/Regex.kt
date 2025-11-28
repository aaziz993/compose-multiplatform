package klib.data.type.primitives.string

// Line & spacing.
public val Regex.Companion.NEW_LINE_PATTERN: String
    get() = """(\r?\n|\n)"""
public val Regex.Companion.NEW_LINE: Regex
    get() = Regex.NEW_LINE_PATTERN.toRegex()
public val Regex.Companion.WHITESPACE_PATTERN: String
    get() = "\\s"
public val Regex.Companion.WHITESPACE: Regex
    get() = "\\s".toRegex()
public val Regex.Companion.NON_WHITESPACE_PATTERN: String
    get() = "\\S"
public val Regex.Companion.NON_WHITESPACE: Regex
    get() = "\\S".toRegex()
public val Regex.Companion.ANY_PATTERN: String
    get() = """[\s\S]"""
public val Regex.Companion.ANY: Regex
    get() = """[\s\S]""".toRegex()

// Character classes.
public val Regex.Companion.WORD_ASCII_PATTERN: String
    get() = "\\w"   // [A-Za-z0-9_]
public val Regex.Companion.WORD_ASCII: Regex
    get() = "\\w".toRegex()   // [A-Za-z0-9_]
public val Regex.Companion.NON_WORD_ASCII_PATTERN: String
    get() = "\\W"
public val Regex.Companion.NON_WORD_ASCII: Regex
    get() = "\\W".toRegex()
public val Regex.Companion.DIGIT_PATTERN: String
    get() = "\\p{Nd}"
public val Regex.Companion.DIGIT: Regex
    get() = "\\p{Nd}".toRegex()
public val Regex.Companion.LETTER_PATTERN: String
    get() = "\\p{L}"
public val Regex.Companion.LETTER: Regex
    get() = "\\p{L}".toRegex()
public val Regex.Companion.LETTER_OR_DIGIT_PATTERN: String
    get() = "[\\p{L}\\p{Nd}]"
public val Regex.Companion.LETTER_OR_DIGIT: Regex
    get() = "[\\p{L}\\p{Nd}]".toRegex()
public val Regex.Companion.LOWERCASE_LETTER_PATTERN: String
    get() = "\\p{Ll}"
public val Regex.Companion.LOWERCASE_LETTER: Regex
    get() = "\\p{Ll}".toRegex()
public val Regex.Companion.UPPERCASE_LETTER_PATTERN: String
    get() = "\\p{Lu}"
public val Regex.Companion.UPPERCASE_LETTER: Regex
    get() = "\\p{Lu}".toRegex()
public val Regex.Companion.ASCII_PATTERN: String
    get() = "\\p{ASCII}"
public val Regex.Companion.ASCII: Regex
    get() = "\\p{ASCII}".toRegex()

// Case transitions.
public val Regex.Companion.LOWER_TO_UPPER_PATTERN: String
    get() = """(?<=$LOWERCASE_LETTER_PATTERN)(?=$UPPERCASE_LETTER_PATTERN)"""
public val Regex.Companion.LOWER_TO_UPPER: Regex
    get() = """(?<=$LOWERCASE_LETTER_PATTERN)(?=$UPPERCASE_LETTER_PATTERN)""".toRegex()
public val Regex.Companion.UPPER_TO_LOWER_PATTERN: String
    get() = """(?<=$UPPERCASE_LETTER_PATTERN)(?=$LOWERCASE_LETTER_PATTERN)"""
public val Regex.Companion.UPPER_TO_LOWER: Regex
    get() = """(?<=$UPPERCASE_LETTER_PATTERN)(?=$LOWERCASE_LETTER_PATTERN)""".toRegex()

// Identifiers.
public val Regex.Companion.ID_PATTERN: String
    get() = "[_\\p{L}][_\\p{L}\\p{N}]*"
public val Regex.Companion.ID: Regex
    get() = "[_\\p{L}][_\\p{L}\\p{N}]*".toRegex()

// String literals.
public val Regex.Companion.SINGLE_QUOTED_STRING_PLAIN_PATTERN: String
    get() = """(?:[^'\\]|\\.)*"""
public val Regex.Companion.SINGLE_QUOTED_STRING_PATTERN: String
    get() = """'$SINGLE_QUOTED_STRING_PLAIN_PATTERN'"""
public val Regex.Companion.SINGLE_QUOTED_STRING: Regex
    get() = """'$SINGLE_QUOTED_STRING_PLAIN_PATTERN'""".toRegex()
public val Regex.Companion.DOUBLE_QUOTED_STRING_PLAIN_PATTERN: String
    get() = """(?:[^"\\]|\\.)*"""
public val Regex.Companion.DOUBLE_QUOTED_STRING_PATTERN: String
    get() = """"$DOUBLE_QUOTED_STRING_PLAIN_PATTERN""""
public val Regex.Companion.DOUBLE_QUOTED_STRING: Regex
    get() = """"$DOUBLE_QUOTED_STRING_PLAIN_PATTERN"""".toRegex()

// Color.
public val Regex.Companion.HEX_COLOR_PATTERN: String
    get() = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$"
public val Regex.Companion.HEX_COLOR: Regex
    get() = Regex.HEX_COLOR_PATTERN.toRegex()
