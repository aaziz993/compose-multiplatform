package klib.data.database.mdb

import kotlinx.datetime.LocalDateTime
import kotlinx.io.IOException

public actual class Table public actual constructor(
    name: String,
    escapeIdentifiers: Boolean,
    database: Database
) : Iterable<Row> {

    public actual val name: String
        get() = TODO("Not yet implemented")
    public actual val isHidden: Boolean
        get() = TODO("Not yet implemented")
    public actual val isSystem: Boolean
        get() = TODO("Not yet implemented")
    public actual val database: Database
        get() = TODO("Not yet implemented")
    public actual var errorHandler: ErrorHandler?
        get() = TODO("Not yet implemented")
        set(value) {}
    public actual var allowAutoNumberInsert: Boolean
        get() = TODO("Not yet implemented")
        set(value) {}
    public actual val columns: List<Column>
        get() = TODO("Not yet implemented")
    public actual val properties: PropertyMap
        get() = TODO("Not yet implemented")
    public actual val createdDate: LocalDateTime
        get() = TODO("Not yet implemented")
    public actual val updatedDate: LocalDateTime
        get() = TODO("Not yet implemented")
    public actual val indexes: List<Index>
        get() = TODO("Not yet implemented")

    public actual fun getIndex(name: String): Index {
        TODO("Not yet implemented")
    }

    public actual val primaryKeyIndex: Index
        get() = TODO("Not yet implemented")

    public actual fun getForeignKeyIndex(otherTable: Table): Index {
        TODO("Not yet implemented")
    }

    public actual val rowCount: Int
        get() = TODO("Not yet implemented")

    public actual fun addRows(rows: List<Array<Any?>>): List<Array<Any?>> {
        TODO("Not yet implemented")
    }

    @Throws(IOException::class)
    public actual fun addRowsFromMaps(rows: List<Map<String, Any?>>): List<Map<String, Any?>> {
        TODO("Not yet implemented")
    }

    public actual fun updateRow(row: Row): Row {
        TODO("Not yet implemented")
    }

    public actual fun deleteRow(row: Row): Row {
        TODO("Not yet implemented")
    }

    public actual fun reset() {
    }

    public actual override operator fun iterator(): Iterator<Row> {
        TODO("Not yet implemented")
    }
}
