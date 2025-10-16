package klib.data.db.exposed.column

import klib.data.type.primitives.time.model.DateTimeRange
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.kotlin.datetime.KotlinLocalDateTimeColumnType

public class DateTimeRangeColumnType : RangeColumnType<LocalDateTime, DateTimeRange>(KotlinLocalDateTimeColumnType()) {

    override fun sqlType(): String = "DATETIMERANGE"

    override fun List<String>.toRange(): DateTimeRange = DateTimeRange(LocalDateTime.parse(first()), LocalDateTime.parse(last()))
}

public fun Table.dateTimeRange(name: String): Column<DateTimeRange> = registerColumn(name, DateTimeRangeColumnType())
