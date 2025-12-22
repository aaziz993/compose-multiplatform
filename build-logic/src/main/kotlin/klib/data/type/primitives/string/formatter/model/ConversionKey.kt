package klib.data.type.primitives.string.formatter.model

public data class ConversionKey(val prefix: Char?, val conversion: Char) {
    public constructor(conversion: Char) : this(null, conversion)

    public fun withConversion(conversion: Char): ConversionKey = ConversionKey(this.prefix, conversion)

    override fun toString(): String = "${prefix?.toString().orEmpty()}$conversion"
}
