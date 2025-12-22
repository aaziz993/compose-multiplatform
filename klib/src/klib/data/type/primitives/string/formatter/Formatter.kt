package klib.data.type.primitives.string.formatter

import klib.data.type.primitives.string.LINE_SEPARATOR
import klib.data.type.primitives.string.formatter.conversions.conversion
import klib.data.type.primitives.string.formatter.conversions.conversionNotNull
import klib.data.type.primitives.string.formatter.argument.ArgumentTaker
import klib.data.type.primitives.string.formatter.model.ConversionKey
import klib.data.type.primitives.string.formatter.model.ConversionMap
import klib.data.type.primitives.string.formatter.model.FlagSet
import klib.data.type.primitives.string.formatter.model.FormatString
import klib.data.type.primitives.string.formatter.model.PartAction
import klib.data.type.primitives.string.formatter.argument.ArgumentIndexHolder
import klib.data.type.primitives.string.lengthSequence

public class Formatter internal constructor(public val conversions: ConversionMap, public val flags: FlagSet) {

    public companion object {

        public val Default: Formatter = buildFormatter {
            conversions {
                /* Some notes for me:
                 * Deny precision for character, integral, date/time, percent, line separator, 'g' conversions
                 * Deny width for line separator conversion.
                 */
                '%'(conversion("%", precisionAction = PartAction.FORBIDDEN), false)

                'n'(conversion(String.LINE_SEPARATOR, PartAction.FORBIDDEN, PartAction.FORBIDDEN), false)

                'b'(
                    conversion
                    { _, arg ->
                        when (arg) {
                            null -> "false"
                            is Boolean -> arg.toString()
                            else -> "true"
                        }
                    },
                )

                's'(
                    conversion(supportedFlags = charArrayOf('#'))
                    { to, str, arg ->
                        if (arg is Formattable)
                            arg.formatTo(to, str)
                        else
                            to.append(arg.toString())
                    },
                )

                'h'(
                    conversionNotNull
                    { _, arg ->
                        arg.hashCode().toString(16)
                    },
                )

                'c'(
                    conversionNotNull(precisionAction = PartAction.FORBIDDEN)
                    { to, str, arg ->
                        when (arg) {
                            is Char -> to.append(arg)
                            is Int, is Short, is Byte -> {
                                val i = (arg as Number).toInt()

                                if (i ushr 16 < (0X10FFFF + 1).ushr(16)) // isValidCodePoint; 0X10FFFF - MAX_CODE_POINT
                                {
                                    if (i ushr 16 == 0) //isBmpCodePoint
                                        to.append(i.toChar())
                                    else {
                                        to.append(((i ushr 10) + (Char.MIN_HIGH_SURROGATE.code - (0x010000 ushr 10))).toChar()) // high surrogate; 0x010000 - MIN_SUPPLEMENTARY_CODE_POINT
                                        to.append(((i and 0x3ff) + Char.MIN_LOW_SURROGATE.code).toChar()) // low surrogate
                                    }
                                }
                                else
                                    throw IllegalFormatCodePointException(str, i)
                            }

                            else -> throw IllegalFormatArgumentException(str, arg)
                        }
                    },
                )
            }
            flags {
                +FLAG_ALTERNATE
                +FLAG_INCLUDE_SIGN
                +FLAG_POSITIVE_LEADING_SPACE
                +FLAG_ZERO_PADDED
                +FLAG_LOCALE_SPECIFIC_GROUPING_SEPARATORS
                +FLAG_NEGATIVE_PARENTHESES
            }
        }
    }

    private val regex: Regex by lazy { createFormatStringRegex(flags, conversions) }

    public fun <T : Appendable> formatTo(to: T, format: String, args: Array<out Any?>): T {
        return to.apply {
            val taker = ArgumentTaker(ArgumentIndexHolder(-1, -1), args)
            var textStart = 0

            for (str in parseFormatString(format, regex)) {
                append(format, textStart, str.start)
                textStart = str.endInclusive + 1

                taker.formatString = str
                val conversion = conversions[str.conversion] ?: throw UnknownConversionException(str)
                conversion.check(str)

                val fWidth = conversion.widthAction == PartAction.STANDARD && str.width != null
                val fPrecision = conversion.precisionAction == PartAction.STANDARD && str.precision != null
                if (fWidth || fPrecision) // Apply standard width or precision post-processing
                {
                    var formatted: CharSequence = StringBuilder().apply { conversion.formatTo(this, str, taker) }

                    if (fPrecision) // Cut $formatted using precision
                    {
                        if (formatted.length - str.precision > 0) {
                            formatted = formatted.lengthSequence(str.precision) //todo: Replace it with formatted.setLength(cut) when this method will come to the stdlib (ha-ha, I'm so naive).
                        }
                    }

                    if (fWidth) // Print $formatted with specified width
                    {
                        val len = str.width - formatted.length
                        if (len > 0) {
                            val leftJustify = FLAG_LEFT_JUSTIFIED in str.flags
                            if (leftJustify)
                                append(formatted)
                            for (n in 1..len)
                                append(' ')
                            if (!leftJustify)
                                append(formatted)
                            continue
                        }
                    }

                    // Print $formatted
                    append(formatted)
                }
                else // Format the conversion directly
                    conversion.formatTo(this, str, taker)
            }
            append(format, textStart, format.length)
        }
    }
}

public fun Formatter.format(str: String, vararg args: Any?) = formatTo(StringBuilder(), str, args).toString()

public fun String.format(vararg args: Any?) = Formatter.Default.format(this, *args)

private fun createFormatStringRegex(flags: FlagSet, conversions: ConversionMap): Regex =
    Regex(
        StringBuilder().apply {
            append("""%(?:(\d+)\$)?([""")
            append(Regex.escape(flags.toCharArray().concatToString()))
            append("""]+)?(\d+)?(?:\.(\d+))?(""")

            val sbPrefixes = StringBuilder(conversions.size)
            for (ch in conversions.keys)
                if (ch.prefix != null)
                    sbPrefixes.append(ch.prefix)
            if (!sbPrefixes.isEmpty()) {
                append("[")
                append(Regex.escape(sbPrefixes.toString()))
                append("]")
            }

            append(""")?(.)""")
        }.toString(),
    )

private fun parseFormatString(format: String, regex: Regex): Iterator<FormatString> =
    object : Iterator<FormatString> {
        private var next: MatchResult? = regex.find(format)

        override fun hasNext(): Boolean = next != null

        override fun next(): FormatString {
            val nxt = next ?: throw NoSuchElementException()

            val result = FormatString(
                argumentIndex = nxt.groupValues[1].toIntOrNull(),
                flags = nxt.groupValues[2],
                width = nxt.groupValues[3].toIntOrNull(),
                precision = nxt.groupValues[4].toIntOrNull(),
                conversion = ConversionKey(nxt.groupValues[5].singleOrNull(), nxt.groupValues[6].single()),
                start = nxt.range.start,
                endInclusive = nxt.range.endInclusive,
            )
            next = nxt.next()
            return result
        }
    }
