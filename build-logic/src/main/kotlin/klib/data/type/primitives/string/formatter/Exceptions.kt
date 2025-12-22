package klib.data.type.primitives.string.formatter

import klib.data.type.primitives.string.formatter.conversions.Conversion
import klib.data.type.primitives.string.formatter.model.ConversionKey
import klib.data.type.primitives.string.formatter.model.FormatString

/*
 * Created by mrAppleXZ on 02.08.18.
 */
public abstract class FormatStringException : RuntimeException {

    private val localMessage: String
    private val formatString: FormatString

    public constructor(formatString: FormatString, localMessage: String) : super() {
        this.formatString = formatString
        this.localMessage = localMessage
    }

    public constructor(formatString: FormatString, localMessage: String, cause: Throwable?) : super(cause) {
        this.formatString = formatString
        this.localMessage = localMessage
    }

    override val message: String?
        get() = "$formatString: $localMessage"
}

public open class PartMismatchException(formatString: FormatString, name: String) : FormatStringException(formatString, "The format string shouldn't have the $name.")

public class PrecisionMismatchException(formatString: FormatString) : PartMismatchException(formatString, "precision")

public class WidthMismatchException(formatString: FormatString) : PartMismatchException(formatString, "width")

public class UnknownConversionException(formatString: FormatString) : FormatStringException(formatString, "Cannot find a conversion '${formatString.conversion}'.")

public class ConversionAlreadyExistsException(key: ConversionKey, existing: Conversion) : RuntimeException("The conversion '$key' already exists: $existing!")

public open class IllegalFormatArgumentException : FormatStringException {
    public constructor(formatString: FormatString, argument: Any?) : super(formatString, "'$argument' of type '${argument?.let { it::class } ?: "NULL"}' is not a valid argument for this conversion.")
    public constructor(formatString: FormatString, message: String) : super(formatString, message)
}

public class IllegalFormatCodePointException(
    formatString: FormatString,
    codePoint: Int
) : IllegalFormatArgumentException(formatString, "Illegal UTF-16 code point: ${codePoint.hashCode().toString(16)}!")

public class FlagMismatchException(
    formatString: FormatString,
    flag: Char
) : FormatStringException(formatString, "The '$flag' flag isn't an allowed flag for this conversion.")

public open class NoSuchArgumentException(
    formatString: FormatString,
    message: String,
    cause: Throwable?
) : FormatStringException(formatString, message, cause)
