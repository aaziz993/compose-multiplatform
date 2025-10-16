package klib.data.db.exposed

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.ExpressionWithColumnType
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.Table

public fun ResultRow.toList(columns: List<ExpressionWithColumnType<*>>): List<Any?> =
    columns.map { column -> this[column] }

public fun ResultRow.toMap(columns: List<Column<*>>): Map<String, Any?> =
    columns.associate { column -> column.name to this[column] }

public fun ResultRow.toMap(table: Table): Map<String, Any?> = toMap(table.columns)
