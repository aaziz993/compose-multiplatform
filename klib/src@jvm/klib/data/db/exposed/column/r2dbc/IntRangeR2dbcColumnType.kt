package klib.data.db.exposed.column.r2dbc

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.IntegerColumnType
import org.jetbrains.exposed.v1.core.Table

public class IntRangeR2dbcColumnType : RangeR2dbcColumnType<Int, IntRange>(IntegerColumnType()) {

    override fun sqlType(): String = "INT4RANGE"

    override fun List<String>.toRange(): IntRange {
        return IntRange(first().toInt(), last().toInt() - 1)
    }
}

public fun Table.intRange(name: String): Column<IntRange> = registerColumn(name, IntRangeR2dbcColumnType())
