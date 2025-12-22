package klib.data.type.primitives.string.formatter.model

public data class FormatString(
    val argumentIndex: Int?,
    val flags: String,
    val width: Int?,
    val precision: Int?,
    val conversion: ConversionKey,
    val start: Int,
    val endInclusive: Int
) {

    override fun toString(): String =
        StringBuilder().apply {
            append("%")
            if (argumentIndex != null)
                append(argumentIndex).append("$")
            if (width != null)
                append(width)
            if (precision != null)
                append(".").append(precision)
            append(conversion)
        }.toString()
}
