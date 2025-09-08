package klib.data.type.primitives.string

import com.fleeksoft.charset.Charsets
import com.fleeksoft.charset.decodeToString
import com.fleeksoft.charset.toByteArray
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import klib.data.type.primitives.string.fuzzywuzzy.Applicable
import klib.data.type.primitives.string.fuzzywuzzy.FuzzySearch
import klib.data.type.primitives.string.fuzzywuzzy.ToStringFunction
import klib.data.type.primitives.string.fuzzywuzzy.model.BoundExtractedResult
import klib.data.type.primitives.string.fuzzywuzzy.model.ExtractedResult
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.time.Duration
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import okio.Buffer
import okio.BufferedSource
import okio.ByteString.Companion.encodeUtf8
import kotlin.collections.joinToString
import kotlin.collections.map
import kotlin.collections.plus
import kotlin.let
import kotlin.text.equals
import kotlin.text.isNotEmpty
import kotlin.text.lowercase
import kotlin.text.replace
import kotlin.text.replaceFirstChar
import kotlin.text.toRegex

// Line break pattern
public const val LBP: String = """(\r?\n|\n)"""

// Any pattern (space and non-space)
public const val AP: String = """[\s\S]"""

// Letter uppercase pattern
public const val LUP: String = "[A-ZА-Я]"

// Letter lowercase pattern
public const val LLP: String = "[a-zа-я]"

// Letter lowercase to uppercase pattern
public const val LLUP: String = "(?<=$LLP)(?=$LUP)"

// Letter uppercase to lowercase pattern
public const val LULP: String = "(?<=$LUP)(?=$LLP)"

// Letter uppercase to uppercase pattern
public const val LUUP: String = "(?<=$LUP)(?=$LUP)"

// Letter pattern
public const val LP: String = "[$LLP$LUP]"

// Letter and digit pattern
public const val LDP: String = """[$LP\d]"""

@Suppress("SameReturnValue")
public val String.Companion.DEFAULT: String
    get() = ""

public fun String.escape(quoteMark: Char = '"'): String =
    replace("\\$quoteMark", "$quoteMark")
        .replace("\\n", "\n")
        .replace("\\r", "\r")
        .replace("\\t", "\t")
        .replace("\\b", "\b")
        .replace("\\f", "\u000C")
        .replace("\\\\", "\\")

public fun String.singleQuote(): String = "'$this'"

public fun String.doubleQuote(): String = "\"$this\""

public fun String.uppercaseFirstChar(): String = replaceFirstChar(Char::uppercase)

public fun String.lowercaseFirstChar(): String = replaceFirstChar(Char::lowercase)

public fun String.ifNotEmpty(transform: (String) -> String): String =
    if (isNotEmpty()) transform(this) else this

public fun String.addPrefix(prefix: String): String =
    "$prefix$this"

public fun String.addSuffix(suffix: String): String =
    "$this$suffix"

public fun String.addPrefixIfNotEmpty(prefix: String): String =
    ifNotEmpty { "$prefix$it" }

public fun String.addSuffixIfNotEmpty(suffix: String): String =
    ifNotEmpty { "$it$suffix" }

private val STRING_FORMAT_REGEX: Regex = "%(\\d)\\$[ds]".toRegex()

public fun String.format(vararg args: String): String =
    STRING_FORMAT_REGEX.replace(this) { matchResult ->
        args[matchResult.groupValues[1].toInt() - 1]
    }

public val String.escapedPattern: String
    get() = Regex.escape(this)

public fun randomString(length: Int, charPool: List<Char> = ('a'..'z') + ('A'..'Z')): String =
    (1..length).map { Random.nextInt(0, charPool.size).let { charPool[it] } }.joinToString("")

private val EXTENSION_TEXT_REGEX: Map<String, Regex> =
    mapOf(
        "json" to """^\s*(\{$AP*\}|\[$AP*\])\s*$""".toRegex(),
        "xml" to """^\s*<\?xml[\s\S]*""".toRegex(),
        "html" to """^\s*<(!DOCTYPE +)?html$AP*""".toRegex(),
        "yaml" to """^( *((#|[^{\s]*:|-).*)?$LBP?)+$""".toRegex(),
        "properties" to """^( *((#|[^{\s\[].*?=).*)?$LBP?)+$""".toRegex(),
        "toml" to """^( *(([#\[\]"{}]|.*=).*)?$LBP?)+$""".toRegex(),
    )

public val String.extension: String?
    get() = EXTENSION_TEXT_REGEX.entries.find { (_, regex) -> regex.matches(this) }?.key

public fun String.toTemporal(kClass: KClass<*>): Any =
    when (kClass) {
        LocalTime::class -> LocalTime.parse(this)
        LocalDate::class -> LocalDate.parse(this)
        LocalDateTime::class -> LocalDateTime.parse(this)
        Duration::class -> Duration.parse(this)
        DatePeriod::class -> DatePeriod.parse(this)
        DateTimePeriod::class -> DateTimePeriod.parse(this)
        else -> IllegalArgumentException("Can't convert ${singleQuote()} to ${kClass.simpleName?.singleQuote()}")
    }

public fun String.toPrime(kClass: KClass<*>): Any =
    when (kClass) {
        Boolean::class -> toBoolean()
        UByte::class -> toUByte()
        UShort::class -> toUShort()
        UInt::class -> toUInt()
        ULong::class -> toULong()
        Byte::class -> toByte()
        Short::class -> toShort()
        Int::class -> toInt()
        Long::class -> toLong()
        Float::class -> toFloat()
        Double::class -> toDouble()
        Char::class -> this[0]
        String::class -> this
        BigInteger::class -> BigInteger.parseString(this)
        BigDecimal::class -> BigDecimal.parseString(this)
        else -> toTemporal(kClass)
    }

// /////////////////////////////////////////////////////MATCH///////////////////////////////////////////////////////////
public fun String.ratio(other: String): Int = FuzzySearch.ratio(this, other)

public fun String.ratio(other: String, stringFunction: ToStringFunction<String>): Int =
    FuzzySearch.ratio(this, other, stringFunction)

public fun String.partialRatio(other: String): Int = FuzzySearch.partialRatio(this, other)

public fun String.partialRatio(other: String, stringFunction: ToStringFunction<String>): Int =
    FuzzySearch.partialRatio(this, other, stringFunction)

public fun String.tokenSortPartialRatio(other: String): Int = FuzzySearch.tokenSortPartialRatio(this, other)

public fun String.tokenSortPartialRatio(other: String, stringFunction: ToStringFunction<String>): Int =
    FuzzySearch.tokenSortPartialRatio(this, other, stringFunction)

public fun String.tokenSortRatio(other: String): Int = FuzzySearch.tokenSortRatio(this, other)

public fun String.tokenSortRatio(other: String, stringFunction: ToStringFunction<String>): Int =
    FuzzySearch.tokenSortRatio(this, other, stringFunction)

public fun String.tokenSetRatio(other: String): Int = FuzzySearch.tokenSetRatio(this, other)

public fun String.tokenSetRatio(other: String, stringFunction: ToStringFunction<String>): Int =
    FuzzySearch.tokenSetRatio(this, other, stringFunction)

public fun String.tokenSetPartialRatio(other: String): Int = FuzzySearch.tokenSetPartialRatio(this, other)

public fun String.tokenSetPartialRatio(other: String, stringFunction: ToStringFunction<String>): Int =
    FuzzySearch.tokenSetPartialRatio(this, other, stringFunction)

public fun String.weightedRatio(other: String): Int = FuzzySearch.weightedRatio(this, other)

public fun String.weightedRatio(other: String, stringFunction: ToStringFunction<String>): Int =
    FuzzySearch.weightedRatio(this, other, stringFunction)

public fun String.extractOne(choices: Iterable<String>): ExtractedResult = FuzzySearch.extractOne(this, choices)

public fun String.extractOne(choices: Iterable<String>, matcher: Applicable): ExtractedResult =
    FuzzySearch.extractOne(this, choices, matcher)

public fun <T> String.extractOne(choices: Iterable<T>, selector: ToStringFunction<T>): BoundExtractedResult<T> =
    FuzzySearch.extractOne(this, choices, selector)

public fun <T> String.extractOne(
    choices: Iterable<T>,
    selector: ToStringFunction<T>,
    matcher: Applicable
): BoundExtractedResult<T> =
    FuzzySearch.extractOne(this, choices, selector, matcher)

public fun String.extractTop(choices: Iterable<String>, limit: Int): List<ExtractedResult> =
    FuzzySearch.extractTop(this, choices, limit)

public fun String.extractTop(choices: Iterable<String>, limit: Int, cutoff: Int): List<ExtractedResult> =
    FuzzySearch.extractTop(this, choices, limit, cutoff)

public fun String.extractTop(choices: Iterable<String>, matcher: Applicable, limit: Int): List<ExtractedResult> =
    FuzzySearch.extractTop(this, choices, matcher, limit)

public fun String.extractTop(
    choices: Iterable<String>,
    matcher: Applicable,
    limit: Int,
    cutoff: Int
): List<ExtractedResult> =
    FuzzySearch.extractTop(this, choices, matcher, limit, cutoff)

public fun <T> String.extractTop(
    choices: Iterable<T>,
    selector: ToStringFunction<T>,
    limit: Int
): List<BoundExtractedResult<T>> =
    FuzzySearch.extractTop(this, choices, selector, limit)

public fun <T> String.extractTop(
    choices: Iterable<T>,
    selector: ToStringFunction<T>,
    limit: Int,
    cutoff: Int
): List<BoundExtractedResult<T>> =
    FuzzySearch.extractTop(this, choices, selector, limit, cutoff)

public fun <T> String.extractTop(
    choices: Iterable<T>,
    selector: ToStringFunction<T>,
    matcher: Applicable,
    limit: Int
): List<BoundExtractedResult<T>> =
    FuzzySearch.extractTop(this, choices, selector, matcher, limit)

public fun <T> String.extractTop(
    choices: Iterable<T>,
    selector: ToStringFunction<T>,
    matcher: Applicable,
    limit: Int,
    cutoff: Int
): List<BoundExtractedResult<T>> =
    FuzzySearch.extractTop(this, choices, selector, matcher, limit, cutoff)

public fun String.extractAll(choices: Iterable<String>): List<ExtractedResult> = FuzzySearch.extractAll(this, choices)

public fun String.extractAll(choices: Iterable<String>, cutoff: Int): List<ExtractedResult> =
    FuzzySearch.extractAll(this, choices, cutoff)

public fun String.extractAll(choices: Iterable<String>, matcher: Applicable): List<ExtractedResult> =
    FuzzySearch.extractAll(this, choices, matcher)

public fun String.extractAll(choices: Iterable<String>, matcher: Applicable, cutoff: Int): List<ExtractedResult> =
    FuzzySearch.extractAll(this, choices, matcher, cutoff)

public fun <T> String.extractAll(choices: Iterable<T>, selector: ToStringFunction<T>): List<BoundExtractedResult<T>> =
    FuzzySearch.extractAll(this, choices, selector)

public fun <T> String.extractAll(
    choices: Iterable<T>,
    selector: ToStringFunction<T>,
    cutoff: Int
): List<BoundExtractedResult<T>> =
    FuzzySearch.extractAll(this, choices, selector, cutoff)

public fun <T> String.extractAll(
    choices: Iterable<T>,
    selector: ToStringFunction<T>,
    matcher: Applicable
): List<BoundExtractedResult<T>> =
    FuzzySearch.extractAll(this, choices, selector, matcher)

public fun <T> String.extractAll(
    choices: Iterable<T>,
    selector: ToStringFunction<T>,
    matcher: Applicable,
    cutoff: Int
): List<BoundExtractedResult<T>> =
    FuzzySearch.extractAll(this, choices, selector, matcher, cutoff)

public fun String.extractSorted(choices: Iterable<String>): List<ExtractedResult> =
    FuzzySearch.extractSorted(this, choices)

public fun String.extractSorted(choices: Iterable<String>, cutoff: Int): List<ExtractedResult> =
    FuzzySearch.extractSorted(this, choices, cutoff)

public fun String.extractSorted(choices: Iterable<String>, matcher: Applicable): List<ExtractedResult> =
    FuzzySearch.extractSorted(this, choices, matcher)

public fun String.extractSorted(choices: Iterable<String>, matcher: Applicable, cutoff: Int): List<ExtractedResult> =
    FuzzySearch.extractSorted(this, choices, matcher, cutoff)

public fun <T> String.extractSorted(
    choices: Iterable<T>,
    selector: ToStringFunction<T>
): List<BoundExtractedResult<T>> =
    FuzzySearch.extractSorted(this, choices, selector)

public fun <T> String.extractSorted(
    choices: Iterable<T>,
    selector: ToStringFunction<T>,
    cutoff: Int
): List<BoundExtractedResult<T>> =
    FuzzySearch.extractSorted(this, choices, selector, cutoff)

public fun <T> String.extractSorted(
    choices: Iterable<T>,
    selector: ToStringFunction<T>,
    matcher: Applicable
): List<BoundExtractedResult<T>> =
    FuzzySearch.extractSorted(this, choices, selector, matcher)

public fun <T> String.extractSorted(
    choices: Iterable<T>,
    selector: ToStringFunction<T>,
    matcher: Applicable,
    cutoff: Int
): List<BoundExtractedResult<T>> =
    FuzzySearch.extractSorted(this, choices, selector, matcher, cutoff)

public fun matcher(
    matchAll: Boolean = true,
    regexMatch: Boolean = false,
    ignoreCase: Boolean = false,
): (String, String) -> Boolean =
    when {
        regexMatch -> {
            val regexMatcher: (String) -> Regex = if (ignoreCase) {
                { pattern -> Regex(pattern, RegexOption.IGNORE_CASE) }
            } else {
                { pattern -> Regex(pattern) }
            }

            if (matchAll) {
                { str, pattern -> regexMatcher(pattern).matches(str) }
            } else {
                { str, pattern -> regexMatcher(pattern).containsMatchIn(str) }
            }
        }

        matchAll -> { str1, str2 -> str1.equals(str2, ignoreCase) }

        else -> { str1, str2 -> str1.contains(str2, ignoreCase) }
    }

// /////////////////////////////////////////////////////STRING//////////////////////////////////////////////////////////
public fun ByteArray.decode(charset: Charset = Charset.UTF_8): String = decodeToString(Charsets.forName(charset.name))

// ///////////////////////////////////////////////////////ENUM//////////////////////////////////////////////////////////
public inline fun <reified T : Enum<T>> String.toEnum(): T = enumValueOf(this)

// ///////////////////////////////////////////////////////ARRAY//////////////////////////////////////////////////////////
public fun String.encode(charset: Charset = Charset.UTF_8): ByteArray = toByteArray(Charsets.forName(charset.name))

public val String.asBufferedSource: BufferedSource
    get() = Buffer().write(encodeUtf8())
