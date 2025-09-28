package klib.data.database.mdb

import klib.data.type.collections.bimap.biMapOf
import kotlinx.io.IOException

public actual class TableMetaData(public val tableMetadata: com.healthmarketscience.jackcess.TableMetaData) {
    public actual val type: TableType
        get() = TYPE_MAP[tableMetadata.type]!!

    public actual val name: String
        get() = tableMetadata.name

    public actual val isLinked: Boolean
        get() = tableMetadata.isLinked

    public actual val isSystem: Boolean
        get() = tableMetadata.isSystem

    public actual val linkedTableName: String
        get() = tableMetadata.linkedTableName

    public actual val linkedDbName: String
        get() = tableMetadata.linkedDbName

    public actual val connectionName: String
        get() = tableMetadata.connectionName

    public actual fun open(db: Database): Table = Table(tableMetadata.open(db.database))

    public actual fun getTableDefinition(db: Database): TableDefinition =
        TableDefinition(tableMetadata.getTableDefinition(db.database))

    public companion object {
        internal val TYPE_MAP = biMapOf(
            com.healthmarketscience.jackcess.TableMetaData.Type.LOCAL to TableType.LOCAL,
            com.healthmarketscience.jackcess.TableMetaData.Type.LINKED to TableType.LINKED,
            com.healthmarketscience.jackcess.TableMetaData.Type.LINKED_ODBC to TableType.LINKED_ODBC
        )
    }
}
