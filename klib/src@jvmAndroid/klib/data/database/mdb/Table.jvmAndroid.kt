package klib.data.database.mdb

import com.healthmarketscience.jackcess.TableBuilder
import klib.data.type.collections.biMapOf
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import okio.IOException

public actual class Table(public val table: com.healthmarketscience.jackcess.Table) : Iterable<Row> {
    public actual constructor(name: String, escapeIdentifiers: Boolean, database: Database) :
            this(TableBuilder(name, escapeIdentifiers).toTable(database.database))

    public actual val name: String
        get() = table.name

    public actual val isHidden: Boolean
        get() = table.isHidden

    public actual val isSystem: Boolean
        get() = table.isSystem

    public actual val database: Database
        get() = Database(table.database)

    public actual var errorHandler: ErrorHandler?
        get() = JavaErrorHandler(table.errorHandler)
        set(value) {
            table.errorHandler = value?.toErrorHandler()
        }

    public actual var allowAutoNumberInsert: Boolean
        get() = table.isAllowAutoNumberInsert
        set(value) {
            table.isAllowAutoNumberInsert = value
        }

    public actual val columns: List<Column>
        get() = table.columns.map(::Column)

    public actual val properties: PropertyMap
        @Throws(IOException::class)
        get() = PropertyMap(table.properties)

    public actual val createdDate: LocalDateTime
        get() = table.createdDate.toKotlinLocalDateTime()

    public actual val updatedDate: LocalDateTime
        get() = table.updatedDate.toKotlinLocalDateTime()

    public actual val indexes: List<Index>
        get() = table.indexes.map(::Index)

    public actual fun getIndex(name: String): Index = Index(table.getIndex(name))

    public actual val primaryKeyIndex: Index
        get() = Index(table.primaryKeyIndex)

    public actual fun getForeignKeyIndex(otherTable: Table): Index =
        Index(table.getForeignKeyIndex(otherTable.table))

    public actual val rowCount: Int
        get() = table.rowCount

    public actual fun addRows(rows: List<Array<Any?>>): List<Array<Any?>> = table.addRows(rows)

    @Throws(IOException::class)
    public actual fun addRowsFromMaps(rows: List<Map<String, Any?>>): List<Map<String, Any?>> =
        table.addRowsFromMaps(rows)

    public actual fun updateRow(row: Row): Row = Row(table.updateRow(row.row))

    public actual fun deleteRow(row: Row): Row = Row(table.deleteRow(row.row))

    public actual fun reset(): Unit = table.reset()

    actual override fun iterator(): Iterator<Row> = table.asSequence().map(::Row).iterator()

    public companion object {
        internal val COLUMN_ORDER_MAP = biMapOf(
            com.healthmarketscience.jackcess.Table.ColumnOrder.DATA to ColumnOrder.DATA,
            com.healthmarketscience.jackcess.Table.ColumnOrder.DISPLAY to ColumnOrder.DISPLAY
        )
    }
}