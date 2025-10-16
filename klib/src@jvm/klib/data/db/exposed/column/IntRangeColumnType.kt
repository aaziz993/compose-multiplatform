package klib.data.db.exposed.column

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.IntegerColumnType
import org.jetbrains.exposed.v1.core.Table

public class IntRangeColumnType : RangeColumnType<Int, IntRange>(IntegerColumnType()) {
    override fun sqlType(): String = "INT4RANGE"

    override fun List<String>.toRange(): IntRange =IntRange(first().toInt(), last().toInt())
}

public fun Table.intRange(name: String): Column<IntRange> = registerColumn(name, IntRangeColumnType())
