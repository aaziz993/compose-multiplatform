package klib.data.type.primitives.string.formatter.conversions

import klib.data.type.primitives.string.formatter.utils.ArgumentTaker
import klib.data.type.primitives.string.formatter.utils.FormatString
import klib.data.type.primitives.string.formatter.utils.PartAction

/*
 * Created by mrAppleXZ on 04.08.18.
 */
public interface Conversion {

    public val widthAction: PartAction

    public val precisionAction: PartAction

    public val canTakeArguments: Boolean

    public fun formatTo(to: Appendable, str: FormatString, taker: ArgumentTaker)

    public fun check(str: FormatString)
}
