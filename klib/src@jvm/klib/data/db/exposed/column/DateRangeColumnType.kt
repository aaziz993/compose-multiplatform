package klib.data.db.exposed.column

import klib.data.type.primitives.time.model.DateRange
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.kotlin.datetime.KotlinLocalDateColumnType

public class DateRangeColumnType : RangeColumnType<LocalDate, DateRange>(KotlinLocalDateColumnType()) {

    override fun sqlType(): String = "DATERANGE"

    override fun List<String>.toRange(): DateRange = DateRange(LocalDate.parse(first()), LocalDate.parse(last()))
}

public fun Table.dateRange(name: String): Column<DateRange> = registerColumn(name, DateRangeColumnType())
