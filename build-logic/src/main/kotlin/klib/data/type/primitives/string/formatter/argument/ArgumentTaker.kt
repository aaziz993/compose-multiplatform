package klib.data.type.primitives.string.formatter.argument

import klib.data.type.primitives.string.formatter.FLAG_REUSE_ARGUMENT
import klib.data.type.primitives.string.formatter.NoSuchArgumentException
import klib.data.type.primitives.string.formatter.model.FormatString

public class ArgumentTaker internal constructor(
    private val holder: ArgumentIndexHolder,
    private val args: Array<out Any?>
) {

    internal lateinit var formatString: FormatString

    public fun take(): Any? {
        when {
            //use explicit argument index
            formatString.argumentIndex != null -> {
                try {
                    holder.lastTaken = formatString.argumentIndex!! - 1
                    return args[holder.lastTaken]
                }
                catch (e: IndexOutOfBoundsException) {
                    throw NoSuchArgumentException(formatString, "Can't use the argument at index ${holder.lastTaken}!", e)
                }
            }
            //reuse previous argument index
            FLAG_REUSE_ARGUMENT in formatString.flags -> {
                try {
                    return args[holder.lastTaken]
                }
                catch (e: IndexOutOfBoundsException) {
                    throw NoSuchArgumentException(formatString, "Can't reuse previously taken argument (${holder.lastTaken})!", e)
                }
            }
            //take the next argument
            else -> {
                holder.lastOrdinary++
                holder.lastTaken = holder.lastOrdinary
                try {
                    return args[holder.lastTaken]
                }
                catch (e: IndexOutOfBoundsException) {
                    throw NoSuchArgumentException(formatString, "Can't take the next ordinary argument (${holder.lastTaken})!", e)
                }
            }
        }
    }
}
