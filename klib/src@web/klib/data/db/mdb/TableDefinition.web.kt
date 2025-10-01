package klib.data.db.mdb

import kotlinx.datetime.LocalDateTime

public actual class TableDefinition {
    public actual val name: String
        get() = TODO("Not yet implemented")
    public actual val isHidden: Boolean
        get() = TODO("Not yet implemented")
    public actual val isSystem: Boolean
        get() = TODO("Not yet implemented")
    public actual val database: Database
        get() = TODO("Not yet implemented")
    public actual val columns: List<Column>
        get() = TODO("Not yet implemented")

    public actual fun getColumn(name: String): Column {
        TODO("Not yet implemented")
    }

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
}
