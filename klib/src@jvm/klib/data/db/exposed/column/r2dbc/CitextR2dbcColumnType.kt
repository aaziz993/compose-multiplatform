package klib.data.db.exposed.column.r2dbc

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.VarCharColumnType

public class CitextR2dbcColumnType(colLength: Int) : VarCharColumnType(colLength) {
    override fun sqlType(): String = "CITEXT"
}

public fun Table.citext(name: String, length: Int): Column<String> =
    registerColumn(name, CitextR2dbcColumnType(length))
