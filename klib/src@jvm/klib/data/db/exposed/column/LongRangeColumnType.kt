package klib.data.db.exposed.column

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.LongColumnType
import org.jetbrains.exposed.v1.core.Table

public class LongRangeColumnType : RangeColumnType<Long, LongRange>(LongColumnType()) {
    override fun sqlType(): String = "INT8RANGE"

    override fun List<String>.toRange(): LongRange =LongRange(first().toLong(), last().toLong())
}

public fun Table.longRange(name: String): Column<LongRange> = registerColumn(name, LongRangeColumnType())
