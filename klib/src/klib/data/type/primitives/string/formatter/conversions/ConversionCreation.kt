package klib.data.type.primitives.string.formatter.conversions

import klib.data.type.primitives.string.formatter.argument.ArgumentTaker
import klib.data.type.primitives.string.formatter.model.FormatString
import klib.data.type.primitives.string.formatter.model.PartAction


public fun conversion(
    replacement: String,
    widthAction: PartAction = PartAction.STANDARD,
    precisionAction: PartAction = PartAction.STANDARD,
): Conversion = ConversionConstant(replacement, widthAction, precisionAction)

public inline fun conversion(
    supportedFlags: CharArray = charArrayOf(),
    widthAction: PartAction = PartAction.STANDARD,
    precisionAction: PartAction = PartAction.STANDARD,
    crossinline executor: (to: Appendable, str: FormatString, arg: Any?) -> Unit,
): Conversion = object : ConversionExecuting(supportedFlags, widthAction, precisionAction) {
    override fun formatTo(to: Appendable, str: FormatString, taker: ArgumentTaker) {
        executor(to, str, taker.take())
    }
}

public inline fun conversion(
    supportedFlags: CharArray = charArrayOf(),
    widthAction: PartAction = PartAction.STANDARD,
    precisionAction: PartAction = PartAction.STANDARD,
    crossinline executor: (str: FormatString, arg: Any?) -> String,
): Conversion = object : ConversionExecuting(supportedFlags, widthAction, precisionAction) {
    override fun formatTo(to: Appendable, str: FormatString, taker: ArgumentTaker) {
        to.append(executor(str, taker.take()))
    }
}

public inline fun conversionNotNull(
    supportedFlags: CharArray = charArrayOf(),
    widthAction: PartAction = PartAction.STANDARD,
    precisionAction: PartAction = PartAction.STANDARD,
    crossinline executor: (to: Appendable, str: FormatString, arg: Any) -> Unit,
): Conversion = object : ConversionExecutingNotNull(supportedFlags, widthAction, precisionAction) {
    override fun formatTo(to: Appendable, str: FormatString, arg: Any) {
        executor(to, str, arg)
    }
}

public inline fun conversionNotNull(
    supportedFlags: CharArray = charArrayOf(),
    widthAction: PartAction = PartAction.STANDARD,
    precisionAction: PartAction = PartAction.STANDARD,
    crossinline executor: (str: FormatString, arg: Any) -> String,
): Conversion = object : ConversionExecutingNotNull(supportedFlags, widthAction, precisionAction) {
    override fun formatTo(to: Appendable, str: FormatString, arg: Any) {
        to.append(executor(str, arg))
    }
}
