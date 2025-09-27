package klib.data.type.primitives.string

// Line & spacing.
public val Regex.Companion.NEW_LINE: String
    get() = """(\r?\n|\n)"""
public val Regex.Companion.NEW_LINE_REGEX: Regex
    get() = Regex.NEW_LINE.toRegex()
public val Regex.Companion.WHITESPACE: String
    get() = "\\s"
public val Regex.Companion.WHITESPACE_REGEX: Regex
    get() = "\\s".toRegex()
public val Regex.Companion.NON_WHITESPACE: String
    get() = "\\S"
public val Regex.Companion.NON_WHITESPACE_REGEX: Regex
    get() = "\\S".toRegex()
public val Regex.Companion.ANY: String
    get() = """[\s\S]"""
public val Regex.Companion.ANY_REGEX: Regex
    get() = """[\s\S]""".toRegex()

// Character classes.
public val Regex.Companion.WORD_ASCII: String
    get() = "\\w"   // [A-Za-z0-9_]
public val Regex.Companion.WORD_ASCII_REGEX: Regex
    get() = "\\w".toRegex()   // [A-Za-z0-9_]
public val Regex.Companion.NON_WORD_ASCII: String
    get() = "\\W"
public val Regex.Companion.NON_WORD_ASCII_REGEX: Regex
    get() = "\\W".toRegex()
public val Regex.Companion.DIGIT: String
    get() = "\\p{Nd}"
public val Regex.Companion.DIGIT_REGEX: Regex
    get() = "\\p{Nd}".toRegex()
public val Regex.Companion.LETTER: String
    get() = "\\p{L}"
public val Regex.Companion.LETTER_REGEX: Regex
    get() = "\\p{L}".toRegex()
public val Regex.Companion.LETTER_OR_DIGIT: String
    get() = "[\\p{L}\\p{Nd}]"
public val Regex.Companion.LETTER_OR_DIGIT_REGEX: Regex
    get() = "[\\p{L}\\p{Nd}]".toRegex()
public val Regex.Companion.LOWERCASE_LETTER: String
    get() = "\\p{Ll}"
public val Regex.Companion.LOWERCASE_LETTER_REGEX: Regex
    get() = "\\p{Ll}".toRegex()
public val Regex.Companion.UPPERCASE_LETTER: String
    get() = "\\p{Lu}"
public val Regex.Companion.UPPERCASE_LETTER_REGEX: Regex
    get() = "\\p{Lu}".toRegex()
public val Regex.Companion.ASCII: String
    get() = "\\p{ASCII}"
public val Regex.Companion.ASCII_REGEX: Regex
    get() = "\\p{ASCII}".toRegex()

// Case transitions.─
public val Regex.Companion.LOWER_TO_UPPER: String
    get() = """(?<=$LOWERCASE_LETTER)(?=$UPPERCASE_LETTER)"""
public val Regex.Companion.LOWER_TO_UPPER_REGEX: Regex
    get() = """(?<=$LOWERCASE_LETTER)(?=$UPPERCASE_LETTER)""".toRegex()
public val Regex.Companion.UPPER_TO_LOWER: String
    get() = """(?<=$UPPERCASE_LETTER)(?=$LOWERCASE_LETTER)"""
public val Regex.Companion.UPPER_TO_LOWER_REGEX: Regex
    get() = """(?<=$UPPERCASE_LETTER)(?=$LOWERCASE_LETTER)""".toRegex()

// Identifiers.──
public val Regex.Companion.ID: String
    get() = "[_\\p{L}][_\\p{L}\\p{N}]*"
public val Regex.Companion.ID_REGEX: Regex
    get() = "[_\\p{L}][_\\p{L}\\p{N}]*".toRegex()

// String literals.─
public val Regex.Companion.SINGLE_QUOTED_STRING_PLAIN: String
    get() = """(?:[^'\\]|\\.)*"""
public val Regex.Companion.SINGLE_QUOTED_STRING: String
    get() = """'$SINGLE_QUOTED_STRING_PLAIN'"""
public val Regex.Companion.SINGLE_QUOTED_STRING_REGEX: Regex
    get() = """'$SINGLE_QUOTED_STRING_PLAIN'""".toRegex()
public val Regex.Companion.DOUBLE_QUOTED_STRING_PLAIN: String
    get() = """(?:[^"\\]|\\.)*"""
public val Regex.Companion.DOUBLE_QUOTED_STRING: String
    get() = """"$DOUBLE_QUOTED_STRING_PLAIN""""
public val Regex.Companion.DOUBLE_QUOTED_STRING_REGEX: Regex
    get() = """"$DOUBLE_QUOTED_STRING_PLAIN"""".toRegex()
