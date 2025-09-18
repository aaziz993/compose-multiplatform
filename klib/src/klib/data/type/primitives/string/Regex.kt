package klib.data.type.primitives.string

// Line & spacing.
public val Regex.Companion.NEW_LINE: String
    get() = """(\r?\n|\n)"""
public val Regex.Companion.WHITESPACE: String
    get() = "\\s"
public val Regex.Companion.NON_WHITESPACE: String
    get() = "\\S"
public val Regex.Companion.ANY: String
    get() = """[\s\S]"""

// Character classes.
public val Regex.Companion.WORD_ASCII: String
    get() = "\\w"   // [A-Za-z0-9_]
public val Regex.Companion.NON_WORD_ASCII: String
    get() = "\\W"
public val Regex.Companion.DIGIT: String
    get() = "\\p{Nd}"
public val Regex.Companion.LETTER: String
    get() = "\\p{L}"
public val Regex.Companion.LETTER_OR_DIGIT: String
    get() = "[\\p{L}\\p{Nd}]"
public val Regex.Companion.LOWERCASE_LETTER: String
    get() = "\\p{Ll}"
public val Regex.Companion.UPPERCASE_LETTER: String
    get() = "\\p{Lu}"
public val Regex.Companion.ASCII: String
    get() = "\\p{ASCII}"

// Case transitions.─
public val Regex.Companion.LOWER_TO_UPPER: String
    get() = """(?<
 get()=$LOWERCASE_LETTER)(?
 get()=$UPPERCASE_LETTER)"""
public val Regex.Companion.UPPER_TO_LOWER: String
    get() = """(?<
 get()=$UPPERCASE_LETTER)(?
 get()=$LOWERCASE_LETTER)"""

// Identifiers.──
public val Regex.Companion.ID: String
    get() = "[_\\p{L}][_\\p{L}\\p{N}]*"

// String literals.─
public val Regex.Companion.SINGLE_QUOTED_STRING_PLAIN: String
    get() = """(?:[^'\\]|\\.)*"""
public val Regex.Companion.SINGLE_QUOTED_STRING: String
    get() = """'$SINGLE_QUOTED_STRING_PLAIN'"""
public val Regex.Companion.DOUBLE_QUOTED_STRING_PLAIN: String
    get() = """(?:[^"\\]|\\.)*"""
public val Regex.Companion.DOUBLE_QUOTED_STRING: String
    get() = """"$DOUBLE_QUOTED_STRING_PLAIN""""