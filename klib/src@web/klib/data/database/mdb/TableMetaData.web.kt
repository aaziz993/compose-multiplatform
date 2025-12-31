package klib.data.database.mdb

import kotlinx.io.IOException

public actual class TableMetaData {
    public actual val type: TableType
        get() = TODO("Not yet implemented")
    public actual val name: String
        get() = TODO("Not yet implemented")
    public actual val isLinked: Boolean
        get() = TODO("Not yet implemented")
    public actual val isSystem: Boolean
        get() = TODO("Not yet implemented")
    public actual val linkedTableName: String
        get() = TODO("Not yet implemented")
    public actual val linkedDbName: String
        get() = TODO("Not yet implemented")
    public actual val connectionName: String
        get() = TODO("Not yet implemented")

    public actual fun open(db: Database): Table {
        TODO("Not yet implemented")
    }

    @Throws(IOException::class)
    public actual fun getTableDefinition(db: Database): TableDefinition {
        TODO("Not yet implemented")
    }
}
