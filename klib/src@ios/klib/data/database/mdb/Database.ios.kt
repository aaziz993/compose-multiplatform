package klib.data.database.mdb

import klib.data.database.mdb.query.Query
import klib.data.type.primitives.string.Charset
import kotlinx.datetime.TimeZone
import okio.IOException

public actual class Database public actual constructor(
    path: String,
    fileFormat: FileFormat?,
    readOnly: Boolean?,
    autoSync: Boolean?,
    ignoreBrokenSystemCatalogIndex: Boolean?,
    timeZone: TimeZone?
) : Iterable<Table>, AutoCloseable {
    public actual val path: String
        get() = TODO("Not yet implemented")
    public actual val tableNames: Set<String>
        get() = TODO("Not yet implemented")
    public actual val systemTableNames: Set<String>
        get() = TODO("Not yet implemented")

    public actual override operator fun iterator(): Iterator<Table> {
        TODO("Not yet implemented")
    }

    @Throws(exceptionClasses = [IOException::class])
    public actual fun getTable(name: String): Table {
        TODO("Not yet implemented")
    }

    @Throws(exceptionClasses = [IOException::class])
    public actual fun getTableMetaData(name: String): TableMetaData {
        TODO("Not yet implemented")
    }

    @Throws(exceptionClasses = [IOException::class])
    public actual fun getRelationships(
        table1: Table,
        table2: Table
    ): List<Relationship> {
        TODO("Not yet implemented")
    }

    @Throws(exceptionClasses = [IOException::class])
    public actual fun getRelationships(table: Table): List<Relationship> {
        TODO("Not yet implemented")
    }

    public actual val relationships: List<Relationship>
        get() = TODO("Not yet implemented")
    public actual val systemRelationships: List<Relationship>
        get() = TODO("Not yet implemented")
    public actual val queries: List<Query>
        get() = TODO("Not yet implemented")

    @Throws(exceptionClasses = [IOException::class])
    public actual fun getSystemTable(tableName: String): Table {
        TODO("Not yet implemented")
    }

    public actual val databaseProperties: PropertyMap
        get() = TODO("Not yet implemented")
    public actual val summaryProperties: PropertyMap
        get() = TODO("Not yet implemented")
    public actual val userDefinedProperties: PropertyMap
        get() = TODO("Not yet implemented")
    public actual val databasePassword: String?
        get() = TODO("Not yet implemented")

    @Throws(exceptionClasses = [IOException::class])
    public actual fun createLinkedTable(name: String, linkedDbName: String, linkedTableName: String) {
    }

    @Throws(exceptionClasses = [IOException::class])
    public actual fun flush() {
    }

    public actual override fun close() {
    }

    public actual var errorHandler: ErrorHandler?
        get() = TODO("Not yet implemented")
        set(value) {}
    public actual var linkResolver: LinkResolver
        get() = TODO("Not yet implemented")
        set(value) {}

    @Throws(exceptionClasses = [IOException::class])
    public actual fun isLinkedTable(table: Table): Boolean {
        TODO("Not yet implemented")
    }

    public actual var timeZone: TimeZone
        get() = TODO("Not yet implemented")
        set(value) {}
    public actual var charset: Charset
        get() = TODO("Not yet implemented")
        set(value) {}
    public actual var columnOrder: ColumnOrder
        get() = TODO("Not yet implemented")
        set(value) {}
    public actual var isEnforceForeignKeys: Boolean
        get() = TODO("Not yet implemented")
        set(value) {}
    public actual var isAllowAutoNumberInsert: Boolean
        get() = TODO("Not yet implemented")
        set(value) {}
    public actual var isEvaluateExpressions: Boolean
        get() = TODO("Not yet implemented")
        set(value) {}
    public actual var columnValidatorFactory: ColumnValidatorFactory?
        get() = TODO("Not yet implemented")
        set(value) {}
    public actual val fileFormat: FileFormat
        get() = TODO("Not yet implemented")
}