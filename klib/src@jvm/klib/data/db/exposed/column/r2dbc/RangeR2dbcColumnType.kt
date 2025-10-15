package klib.data.db.exposed.column.r2dbc

import org.jetbrains.exposed.sql.ColumnType

public abstract class RangeR2dbcColumnType<T : Comparable<T>, R : ClosedRange<T>>(
    public val subType: ColumnType<T>,
) : ColumnType<R>() {

    public abstract fun List<String>.toRange(): R

    override fun nonNullValueToString(value: R): String =
        toPostgresqlValue(value)

    override fun nonNullValueAsDefaultString(value: R): String =
        "'${nonNullValueToString(value)}'"

    override fun valueFromDB(value: Any): R = when (value) {
        is String -> value.trim('[', ')').split(',').toRange()
        else -> error("Unexpected DB value type: ${value::class.simpleName}")
    }

    public companion object {

        public fun <T : Comparable<T>, R : ClosedRange<T>> toPostgresqlValue(range: R): String =
            "[${range.start},${range.endInclusive}]"
    }
}
