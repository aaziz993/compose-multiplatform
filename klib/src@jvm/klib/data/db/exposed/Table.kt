package klib.data.db.exposed

import klib.data.db.getTables
import klib.data.db.sortedByFkReferences
import klib.data.type.primitives.string.case.toCamelCase
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.ForeignKeyConstraint
import org.jetbrains.exposed.v1.core.Table

public operator fun Table.get(name: String): Column<*>? = columns.find { column -> column.name.toCamelCase() == name }

public fun getTables(vararg packages: String): List<Table> = getTables(*packages, tableClass = Table::class)

public fun List<Table>.sortedByFkReferences() =
    sortedByFkReferences { foreignKeys.map(ForeignKeyConstraint::targetTable) }
