package klib.data.type.primitives.string.formatter.utils.internal

import klib.data.type.primitives.string.formatter.utils.ConversionMap
import klib.data.type.primitives.string.formatter.utils.FlagSet

internal fun createFormatStringRegex(flags: FlagSet, conversions: ConversionMap): Regex =
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
