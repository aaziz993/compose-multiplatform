package klib.data.type.primitives.string.tokenization.lexer

public class RegexToken(name: String?, private val regex: Regex, ignored: Boolean = false) : Token(name, ignored) {
    public constructor(name: String?, /* @Language("RegExp")  */pattern: String, ignored: Boolean = false)
            : this(name, Regex(pattern), ignored)

    override fun match(input: CharSequence, fromIndex: Int): Int =
        regex.matchAt(input, fromIndex)?.value?.length ?: 0

    override fun toString(): String = "${name ?: ""} [${regex.pattern}]" + if (ignored) " [ignorable]" else ""
}

public fun regexToken(name: String, /* @Language("RegExp")  */pattern: String, ignore: Boolean = false): RegexToken =
    RegexToken(name, pattern, ignore)

public fun regexToken(name: String, pattern: Regex, ignore: Boolean = false): RegexToken =
    RegexToken(name, pattern, ignore)

public fun regexToken(/* @Language("RegExp")  */pattern: String, ignore: Boolean = false): RegexToken =
    RegexToken(null, pattern, ignore)

public fun regexToken(pattern: Regex, ignore: Boolean = false): RegexToken = RegexToken(null, pattern, ignore)

@Deprecated(
    "Use either regexToken or literalToken. This function will be removed soon",
    ReplaceWith("regexToken(pattern, ignore)")
)
public fun token(name: String, /* @Language("RegExp")  */pattern: String, ignore: Boolean = false): RegexToken =
    RegexToken(name, pattern, ignore)

@Deprecated(
    "Use either regexToken or literalToken. This function will be removed soon",
    ReplaceWith("regexToken(pattern, ignore)")
)
public fun token(name: String, pattern: Regex, ignore: Boolean = false): RegexToken = RegexToken(name, pattern, ignore)

@Deprecated(
    "Use either regexToken or literalToken. This function will be removed soon",
    ReplaceWith("regexToken(pattern, ignore)")
)
public fun token(/* @Language("RegExp")  */pattern: String, ignore: Boolean = false): RegexToken =
    RegexToken(null, pattern, ignore)

@Deprecated(
    "Use either regexToken or literalToken. This function will be removed soon",
    ReplaceWith("regexToken(pattern, ignore)")
)
public fun token(pattern: Regex, ignore: Boolean = false): RegexToken = RegexToken(null, pattern, ignore)
