package klib.data.type.primitives.string.formatter

import klib.data.type.primitives.string.formatter.utils.FormatString

/*
 * Created by mrAppleXZ on 14.08.18.
 */
public interface Formattable {

    public fun formatTo(to: Appendable, str: FormatString)
}
