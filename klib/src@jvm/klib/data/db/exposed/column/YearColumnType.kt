package klib.data.db.exposed.column

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.IDateColumnType
import org.jetbrains.exposed.v1.core.StringColumnType
import org.jetbrains.exposed.v1.core.Table
import java.sql.Date
import org.jetbrains.exposed.v1.core.Function
import org.jetbrains.exposed.v1.core.QueryBuilder

public class YearColumnType : StringColumnType(), IDateColumnType {
    override fun sqlType(): String = "YEAR"

    override val hasTimePart: Boolean = false

    override fun valueFromDB(value: Any): String = when (value) {
        is Date -> value.toString().substringBefore('-')
        else -> error("Retrieved unexpected value of type ${value::class.simpleName}")
    }
}

public fun Table.year(name: String): Column<String> = registerColumn(name, YearColumnType())

public object CurrentYear : Function<String>(YearColumnType()) {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        queryBuilder { +"CURRENT_DATE" }
    }
}
