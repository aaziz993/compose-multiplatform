package klib.data.db.exposed.jdbc.column

import klib.data.type.primitives.time.model.TimeRange
import kotlinx.datetime.LocalTime
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.KotlinLocalTimeColumnType

public class TimeRangeColumnType : RangeColumnType<LocalTime, TimeRange>(KotlinLocalTimeColumnType()) {

    override fun sqlType(): String = "TIMERANGE"

    override fun List<String>.toRange(): TimeRange = TimeRange(LocalTime.parse(first()),  LocalTime.parse(last()))
}

public fun Table.timeRange(name: String): Column<TimeRange> = registerColumn(name, TimeRangeColumnType())
