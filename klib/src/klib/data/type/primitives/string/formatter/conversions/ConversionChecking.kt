package klib.data.type.primitives.string.formatter.conversions

import klib.data.type.primitives.string.formatter.FLAG_LEFT_JUSTIFIED
import klib.data.type.primitives.string.formatter.FLAG_REUSE_ARGUMENT
import klib.data.type.primitives.string.formatter.FlagMismatchException
import klib.data.type.primitives.string.formatter.PrecisionMismatchException
import klib.data.type.primitives.string.formatter.WidthMismatchException
import kotlin.text.iterator
import klib.data.type.primitives.string.formatter.utils.FormatString
import klib.data.type.primitives.string.formatter.utils.PartAction

public interface ConversionChecking : Conversion {

    override fun check(str: FormatString) {
        if (widthAction == PartAction.FORBIDDEN && str.width != null)
            throw WidthMismatchException(str)
        if (precisionAction == PartAction.FORBIDDEN && str.precision != null)
            throw PrecisionMismatchException(str)
        for (flag in str.flags) {
            if (flag == FLAG_LEFT_JUSTIFIED && widthAction != PartAction.FORBIDDEN)
                continue
            if (flag == FLAG_REUSE_ARGUMENT && canTakeArguments)
                continue
            if (checkFlag(str, flag))
                continue
            throw FlagMismatchException(str, flag)
        }
    }

    public fun checkFlag(str: FormatString, flag: Char): Boolean = false
}
