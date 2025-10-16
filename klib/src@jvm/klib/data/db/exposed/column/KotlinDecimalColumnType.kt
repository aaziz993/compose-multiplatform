package klib.data.db.exposed.column

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import java.math.MathContext
import java.sql.SQLException
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.ColumnType
import org.jetbrains.exposed.v1.core.Table

public data class KotlinDecimalColumnType(
    /** Total count of significant digits in the whole number. */
    public val precision: Long,
    /** Count of decimal digits in the fractional part. */
    public val scale: Long
) : ColumnType<BigDecimal>() {

    override fun sqlType(): String = "DECIMAL($precision, $scale)"

    override fun valueFromDB(value: Any): BigDecimal = when (value) {
        is BigDecimal -> value
        is Double -> if (value.isNaN())
            throw SQLException("Unexpected value of type Double: NaN of ${value::class.qualifiedName}")
        else value.toBigDecimal(decimalMode = DecimalMode(precision, RoundingMode.ROUND_HALF_TO_EVEN, scale))

        is Float -> if (value.isNaN()) error("Unexpected value of type Float: NaN of ${value::class.qualifiedName}")
        else value.toBigDecimal(decimalMode = DecimalMode(precision, RoundingMode.ROUND_HALF_TO_EVEN, scale))

        is Long -> value.toBigDecimal(decimalMode = DecimalMode(precision, RoundingMode.ROUND_HALF_TO_EVEN, scale))
        is Int -> value.toBigDecimal(decimalMode = DecimalMode(precision, RoundingMode.ROUND_HALF_TO_EVEN, scale))
        is Short -> value.toLong().toBigDecimal(decimalMode = DecimalMode(precision, RoundingMode.ROUND_HALF_TO_EVEN, scale))

        else -> error("Unexpected value of type Decimal: $value of ${value::class.qualifiedName}")
    }

    public companion object {

        internal val INSTANCE = KotlinDecimalColumnType(MathContext.DECIMAL64.precision.toLong(), 20L)
    }
}

public fun Table.kotlinDecimal(name: String, precision: Long, scale: Long): Column<BigDecimal> =
    registerColumn(name, KotlinDecimalColumnType(precision, scale))
