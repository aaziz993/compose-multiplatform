package klib.data.type.primitives.string.formatter

import klib.data.type.primitives.string.formatter.conversions.Conversion
import klib.data.type.primitives.string.formatter.conversions.UppercaseConversion
import klib.data.type.primitives.string.formatter.model.ConversionKey
import klib.data.type.primitives.string.formatter.model.MutableConversionMap
import klib.data.type.primitives.string.formatter.model.MutableFlagSet

@DslMarker
public annotation class FormatterDsl

@FormatterDsl
public class StringFormatterBuilder internal constructor() {

    private val conversions: MutableConversionMap = hashMapOf()
    private val flags: MutableFlagSet = hashSetOf(FLAG_REUSE_ARGUMENT, FLAG_LEFT_JUSTIFIED)

    public fun takeFrom(formatter: StringFormatter) {
        flags.takeFrom(formatter)
        conversions.takeFrom(formatter)
    }

    public fun conversions(init: ConversionsScope.() -> Unit) {
        val scope = ConversionsScope(conversions)
        scope.init()
    }

    public fun flags(init: FlagsScope.() -> Unit) {
        val scope = FlagsScope(flags)
        scope.init()
    }

    public fun createFormatter(): StringFormatter = StringFormatter(conversions, flags)
}

@FormatterDsl
public class ConversionsScope internal constructor(public val conversions: MutableConversionMap) {

    public fun takeFrom(formatter: StringFormatter) {
        conversions.takeFrom(formatter)
    }

    public operator fun Char.invoke(conversion: Conversion, uppercaseVariant: Boolean = true) {
        put(ConversionKey(this), conversion, uppercaseVariant)
    }

    public operator fun String.invoke(conversion: Conversion, uppercaseVariant: Boolean = true) {
        val key = when (this.length) {
            1 -> ConversionKey(this[0])
            2 -> ConversionKey(this[0], this[1])
            else -> throw IllegalArgumentException("$this: The conversion key string should be 1 or 2 characters long.")
        }
        put(key, conversion, uppercaseVariant)
    }

    private fun put(key: ConversionKey, conversion: Conversion, uppercaseVariant: Boolean) {
        val prev = conversions[key]
        if (prev != null)
            throw ConversionAlreadyExistsException(key, prev)
        conversions[key] = conversion
        if (uppercaseVariant)
            put(key.withConversion(key.conversion.uppercaseChar()), UppercaseConversion(conversion), false)
    }
}

@FormatterDsl
public class FlagsScope internal constructor(public val flags: MutableFlagSet) {

    public fun takeFrom(formatter: StringFormatter) {
        flags.takeFrom(formatter)
    }

    public operator fun Char.unaryPlus() {
        flags.add(this)
    }
}

public fun buildFormatter(init: StringFormatterBuilder.() -> Unit): StringFormatter {
    val bld = StringFormatterBuilder()
    bld.init()
    return bld.createFormatter()
}

private fun MutableConversionMap.takeFrom(formatter: StringFormatter) = putAll(formatter.conversions)
private fun MutableFlagSet.takeFrom(formatter: StringFormatter) = addAll(formatter.flags)
