@file:Suppress("OPTIONAL_DECLARATION_USAGE_IN_NON_COMMON_SOURCE")

package klib.data.database.mdb

import com.healthmarketscience.jackcess.TableDefinition
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.io.IOException

public actual class TableDefinition(public val tableDefinition: TableDefinition) {

    public actual val name: String
        get() = tableDefinition.name

    public actual val isHidden: Boolean
        get() = tableDefinition.isHidden

    public actual val isSystem: Boolean
        get() = tableDefinition.isSystem

    public actual val database: Database
        get() = Database(tableDefinition.database)

    public actual val columns: List<Column>
        get() = tableDefinition.columns.map(::Column)

    public actual fun getColumn(name: String): Column = Column(tableDefinition.getColumn(name))

    public actual val properties: PropertyMap
        @Throws(IOException::class)
        get() = PropertyMap(tableDefinition.properties)

    public actual val createdDate: LocalDateTime
        @Throws(IOException::class)
        get() = tableDefinition.createdDate.toKotlinLocalDateTime()

    public actual val updatedDate: LocalDateTime
        @Throws(IOException::class)
        get() = tableDefinition.updatedDate.toKotlinLocalDateTime()

    public actual val indexes: List<Index>
        get() = tableDefinition.indexes.map(::Index)

    public actual fun getIndex(name: String): Index = Index(tableDefinition.getIndex(name))

    public actual val primaryKeyIndex: Index
        get() = Index(tableDefinition.primaryKeyIndex)

    public actual fun getForeignKeyIndex(otherTable: Table): Index =
        Index(tableDefinition.getForeignKeyIndex(otherTable.table))
}
