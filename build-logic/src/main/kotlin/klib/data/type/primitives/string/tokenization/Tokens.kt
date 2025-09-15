package klib.data.type.primitives.string.tokenization

import com.github.h0tk3y.betterParse.lexer.CharToken
import com.github.h0tk3y.betterParse.lexer.LiteralToken
import com.github.h0tk3y.betterParse.lexer.RegexToken
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import klib.data.type.primitives.string.DOUBLE_QUOTED_STRING_PATTERN
import klib.data.type.primitives.string.ID_PATTERN
import klib.data.type.primitives.string.KEY_PATTERN
import klib.data.type.primitives.string.SINGLE_QUOTED_STRING_PATTERN

public object Tokens {

    public const val PREFIX: String = """(?<![\p{L}\p{N}_])"""
    public const val SUFFIX: String = """(?![\p{L}\p{N}_])"""

    // Comment tokens.
    public val hashComment: RegexToken = regexToken("""#[^\r\n]*""")
    public val hashCommentIgnore: RegexToken = regexToken("""#[^\r\n]*""", ignore = true)

    // Whitespace and newline tokens.
    public val ws: RegexToken = regexToken("\\s+")
    public val wsIgnore: RegexToken = regexToken("\\s+", ignore = true)
    public val nl: RegexToken = regexToken("""[\r\n]+""")
    public val nlIgnore: RegexToken = regexToken("""[\r\n]+""", ignore = true)

    // Parentheses and brackets tokens.
    public val leftPar: CharToken = literalToken("(") as CharToken
    public val rightPar: CharToken = literalToken(")") as CharToken
    public val leftBr: CharToken = literalToken("{") as CharToken
    public val rightBr: CharToken = literalToken("}") as CharToken
    public val leftSqBr: CharToken = literalToken("[") as CharToken
    public val rightSqBr: CharToken = literalToken("]") as CharToken

    // Punctuation symbols are used to structure and organize text. They guide the reader through sentences and help convey the intended meaning. Here are some common punctuation symbols with their names:
    public val period: CharToken = literalToken(".") as CharToken
    public val comma: CharToken = literalToken(",") as CharToken
    public val questionMark: CharToken = literalToken("?") as CharToken
    public val exclamationMark: CharToken = literalToken("!") as CharToken
    public val colon: CharToken = literalToken(":") as CharToken
    public val semicolon: CharToken = literalToken(";") as CharToken
    public val apostrophe: CharToken = literalToken("‘") as CharToken
    public val leftQuoteMark: CharToken = literalToken("”") as CharToken
    public val rightQuoteMark: CharToken = literalToken("“") as CharToken
    public val singleQuoteMark: CharToken = literalToken("'") as CharToken
    public val doubleQuoteMark: CharToken = literalToken("\"") as CharToken
    public val hyphen: CharToken = literalToken("-") as CharToken
    public val dash: CharToken = literalToken("—") as CharToken

    // Mathematical Symbols
    // Mathematics uses a variety of symbols to represent numbers, operations, and relationships. Here are some essential mathematical symbols:
    public val plus: CharToken = literalToken("+") as CharToken
    public val minus: CharToken = literalToken("−") as CharToken
    public val multiplication: CharToken = literalToken("×") as CharToken
    public val division: CharToken = literalToken("÷") as CharToken
    public val equals: CharToken = literalToken("=") as CharToken
    public val greaterThan: CharToken = literalToken(">") as CharToken
    public val lessThan: CharToken = literalToken("<") as CharToken
    public val percent: CharToken = literalToken("%") as CharToken
    public val squareRoot: CharToken = literalToken("√") as CharToken
    public val pi: CharToken = literalToken("π") as CharToken

    // Currency Symbols Names
    // Currency symbols represent different units of money around the world. Recognizing these symbols is essential for understanding global economics and trade. Here are some common currency symbols:
    public val dollar: CharToken = literalToken("$") as CharToken
    public val euro: CharToken = literalToken("€") as CharToken
    public val pound: CharToken = literalToken("£") as CharToken
    public val yen: CharToken = literalToken("¥") as CharToken
    public val rupee: CharToken = literalToken("₹") as CharToken
    public val won: CharToken = literalToken("₩") as CharToken
    public val lira: CharToken = literalToken("₺") as CharToken
    public val franc: CharToken = literalToken("₣") as CharToken
    public val bitcoin: CharToken = literalToken("₿") as CharToken
    public val peso: CharToken = literalToken("₱") as CharToken

    // Keyboard Symbols Names
    // Your keyboard is filled with symbols that you use daily for various functions. Knowing their names can help you use them more effectively. Here are some keyboard symbols:
    public val ampersand: CharToken = literalToken("&") as CharToken
    public val at: CharToken = literalToken("@") as CharToken
    public val hash: CharToken = literalToken("#") as CharToken
    public val asterisk: CharToken = literalToken("*") as CharToken
    public val underscore: CharToken = literalToken("_") as CharToken
    public val tilde: CharToken = literalToken("~") as CharToken
    public val caret: CharToken = literalToken("^") as CharToken
    public val backslash: CharToken = literalToken("\\") as CharToken
    public val forwardSlash: CharToken = literalToken("/") as CharToken
    public val pipe: CharToken = literalToken("|") as CharToken

    // Internet and Technology Symbols
    // The digital world has its own set of symbols that are crucial for communication and navigation online. Here are some common internet and technology symbols:
    public val wiFiSignal: LiteralToken = literalToken("📶") as LiteralToken
    public val bluetooth: LiteralToken = literalToken("🅱️") as LiteralToken
    public val powerOnOff: CharToken = literalToken("⏻") as CharToken
    public val usb: LiteralToken = literalToken("🔌") as LiteralToken
    public val search: LiteralToken = literalToken("🔍") as LiteralToken
    public val cloud: LiteralToken = literalToken("☁️") as LiteralToken
    public val download: LiteralToken = literalToken("⬇️") as LiteralToken
    public val upload: LiteralToken = literalToken("⬆️") as LiteralToken
    public val email: LiteralToken = literalToken("📧") as LiteralToken
    public val settings: LiteralToken = literalToken("⚙️") as LiteralToken

    // Miscellaneous Symbols
    // These symbols are used in various contexts, from road signs to product packaging. Knowing them can help you navigate through different situations:
    public val heart: LiteralToken = literalToken("♥️") as LiteralToken
    public val star: CharToken = literalToken("★") as CharToken
    public val peace: LiteralToken = literalToken("☮️") as LiteralToken
    public val recycle: LiteralToken = literalToken("♻️") as LiteralToken
    public val biohazard: LiteralToken = literalToken("☣️") as LiteralToken
    public val caduceus: LiteralToken = literalToken("⚕️") as LiteralToken
    public val yinYang: LiteralToken = literalToken("☯️") as LiteralToken
    public val smileyFace: LiteralToken = literalToken("☺️") as LiteralToken
    public val musicNote: CharToken = literalToken("♪") as CharToken
    public val checkMark: LiteralToken = literalToken("✔️") as LiteralToken

    // Religious Symbols Names
    // Religious symbols hold significant meaning in various cultures and are often used in rituals, art, and literature. Here are some common religious symbols:
    public val cross: LiteralToken = literalToken("✝️") as LiteralToken
    public val crescentAndStar: LiteralToken = literalToken("☪️") as LiteralToken
    public val starOfDavid: LiteralToken = literalToken("✡️") as LiteralToken
    public val om: LiteralToken = literalToken("🕉️") as LiteralToken
    public val lotus: LiteralToken = literalToken("🪷") as LiteralToken
    public val torii: LiteralToken = literalToken("⛩️") as LiteralToken
    public val wheelOfDharma: LiteralToken = literalToken("☸️") as LiteralToken
    public val khanda: CharToken = literalToken("☬") as CharToken
    public val ankh: CharToken = literalToken("☥") as CharToken
    public val menorah: LiteralToken = literalToken("🕎") as LiteralToken

    // Println token.
    public val println: RegexToken = keyword("println")

    // Skip token.
    public val skip: RegexToken = keyword("skip")

    // Declare tokens.
    public val `val`: RegexToken = keyword("val")
    public val `var`: RegexToken = keyword("var")

    // If-else tokens.
    public val `if`: RegexToken = keyword("if")
    public val then: RegexToken = keyword("then")
    public val elif: RegexToken = keyword("elif")
    public val `else`: RegexToken = keyword("else")
    public val fi: RegexToken = keyword("fi")

    // Loop tokens.
    public val `for`: RegexToken = keyword("for")
    public val forEach: RegexToken = keyword("forEach")
    public val `while`: RegexToken = keyword("while")
    public val `do`: RegexToken = keyword("do")
    public val od: RegexToken = keyword("od")
    public val `in`: RegexToken = keyword("in")

    // Try-catch tokens.
    public val `try`: RegexToken = keyword("try")
    public val catch: RegexToken = keyword("catch")
    public val finally: RegexToken = keyword("finally")
    public val yrt: RegexToken = keyword("yrt")
    public val `throw`: RegexToken = keyword("throw")

    // Function tokens.
    public val `fun`: RegexToken = keyword("fun")
    public val begin: RegexToken = keyword("begin")
    public val end: RegexToken = keyword("end")
    public val `return`: RegexToken = keyword("return")

    // Pair.
    public val to: RegexToken = keyword("to")

    // Literal tokens.
    public val `null`: RegexToken = keyword("null")
    public val boolean: RegexToken = keyword("true|false")
    public val integer: RegexToken = regexToken("""\d(?:_?\d)*""")
    public val exponent: RegexToken = suffix("e", ignoreCase = true)
    public val numberSuffix: RegexToken = suffix("ul|u|f|l", ignoreCase = true)
    public val character: RegexToken = regexToken("""'(?:\\.|[^'\\])'""")

    // the regex "[^\\"]*(\\["nrtbf\\][^\\"]*)*" matches:
    // '               – opening singe quote,
    // [^\\"]*         – any number of not escaped characters, nor double quotes
    // (
    //   \\["nrtbf\\]  – backslash followed by special character (\", \n, \r, \\, etc.)
    //   [^\\"]*       – and any number of non-special characters
    // )*              – repeating as a group any number of times
    // '               – closing single quote
    public val singleQuotedStringRegex: RegexToken = regexToken(SINGLE_QUOTED_STRING_PATTERN)

    // the regex "[^\\"]*(\\["nrtbf\\][^\\"]*)*" matches:
    // "               – opening double quote,
    // [^\\"]*         – any number of not escaped characters, nor double quotes
    // (
    //   \\["nrtbf\\]  – backslash followed by special character (\", \n, \r, \\, etc.)
    //   [^\\"]*       – and any number of non-special characters
    // )*              – repeating as a group any number of times
    // "               – closing double quote
    public val doubleQuotedString: RegexToken = regexToken(DOUBLE_QUOTED_STRING_PATTERN)

    // Id token.
    public val id: RegexToken = regexToken(ID_PATTERN)

    // Key token.
    public val key: RegexToken = regexToken(KEY_PATTERN)

    public fun keyword(value: String): RegexToken = regexToken("$PREFIX$value$SUFFIX")

    public fun suffix(value: String, ignoreCase: Boolean = false): RegexToken =
        regexToken("(?${if (ignoreCase) "i" else ""}:$value)$SUFFIX")
}
