package klib.data.type.primitives.string.formatter.conversions

import kotlin.text.iterator
import klib.data.type.primitives.string.formatter.utils.ArgumentTaker
import klib.data.type.primitives.string.formatter.utils.FormatString
import klib.data.type.primitives.string.formatter.utils.PartAction

public class UppercaseConversion(private val baseConversion: Conversion) : Conversion {

    override val widthAction: PartAction
        get() = baseConversion.widthAction

    override val precisionAction: PartAction
        get() = baseConversion.precisionAction

    override val canTakeArguments: Boolean
        get() = baseConversion.canTakeArguments

    override fun formatTo(to: Appendable, str: FormatString, taker: ArgumentTaker): Unit = baseConversion.formatTo(UppercaseAppendable(to), str, taker)

    override fun check(str: FormatString): Unit = baseConversion.check(str)

    private class UppercaseAppendable(private val to: Appendable) : Appendable {

        override fun append(value: Char): Appendable {
            return to.apply {
                append(value.uppercaseChar())
            }
        }

        override fun append(value: CharSequence?): Appendable {
            return to.apply {
                if (value == null)
                    append("NULL")
                else {
                    for (ch in value) {
                        append(ch.uppercaseChar())
                    }
                }
            }
        }

        override fun append(value: CharSequence?, startIndex: Int, endIndex: Int): Appendable {
            return to.apply {
                val s = value ?: "NULL"

                if (startIndex < 0 || startIndex > endIndex || endIndex > s.length)
                    throw IndexOutOfBoundsException("start $startIndex, end $endIndex, s.length() ${s.length}")

                for (i in startIndex..(endIndex - 1)) {
                    append(s[i].uppercaseChar())
                }
            }
        }
    }
}
