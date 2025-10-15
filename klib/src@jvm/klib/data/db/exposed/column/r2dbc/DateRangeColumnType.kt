package klib.data.db.exposed.column.r2dbc

import klib.data.type.primitives.time.model.DateRange
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.KotlinLocalDateColumnType

public class DateRangeColumnType : RangeR2dbcColumnType<LocalDate, DateRange>(KotlinLocalDateColumnType()) {

    override fun sqlType(): String = "DATERANGE"

    override fun List<String>.toRange(): DateRange {
        val endInclusive = LocalDate.parse(last()).minus(1, DateTimeUnit.DAY)
        return DateRange(LocalDate.parse(first()), endInclusive)
    }
}

public fun Table.dateRange(name: String): Column<DateRange> = registerColumn(name, DateRangeColumnType())
