package klib.data.db.exposed.r2dbc.column

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.LongColumnType
import org.jetbrains.exposed.v1.core.Table

public class LongRangeColumnType : RangeR2dbcColumnType<Long, LongRange>(LongColumnType()) {

    override fun sqlType(): String = "INT8RANGE"

    override fun List<String>.toRange(): LongRange =
        LongRange(first().toLong(), last().toLong() - 1)
}

public fun Table.longRange(name: String): Column<LongRange> = registerColumn(name, LongRangeColumnType())
