package klib.data.database.mdb

import com.healthmarketscience.jackcess.Database
import com.healthmarketscience.jackcess.DatabaseBuilder
import com.healthmarketscience.jackcess.DateTimeType
import com.healthmarketscience.jackcess.query.AppendQuery
import com.healthmarketscience.jackcess.query.CrossTabQuery
import com.healthmarketscience.jackcess.query.DataDefinitionQuery
import com.healthmarketscience.jackcess.query.DeleteQuery
import com.healthmarketscience.jackcess.query.MakeTableQuery
import com.healthmarketscience.jackcess.query.PassthroughQuery
import com.healthmarketscience.jackcess.query.SelectQuery
import com.healthmarketscience.jackcess.query.UnionQuery
import com.healthmarketscience.jackcess.query.UpdateQuery
import java.io.File
import klib.data.database.mdb.Table.Companion.COLUMN_ORDER_MAP
import klib.data.database.mdb.query.JavaAppendQuery
import klib.data.database.mdb.query.JavaCrossTabQuery
import klib.data.database.mdb.query.JavaDataDefinitionQuery
import klib.data.database.mdb.query.JavaDeleteQuery
import klib.data.database.mdb.query.JavaMakeTableQuery
import klib.data.database.mdb.query.JavaPassthroughQuery
import klib.data.database.mdb.query.JavaSelectQuery
import klib.data.database.mdb.query.JavaUnionQuery
import klib.data.database.mdb.query.JavaUpdateQuery
import klib.data.database.mdb.query.Query
import klib.data.type.collections.bimap.biMapOf
import klib.data.type.functions.tryInvoke
import klib.data.type.primitives.string.Charset
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaZoneId
import kotlinx.io.IOException

public actual class Database(public val database: Database) :
    Iterable<Table>, AutoCloseable {
    public actual constructor(
        path: String,
        fileFormat: FileFormat?,
        readOnly: Boolean?,
        autoSync: Boolean?,
        ignoreBrokenSystemCatalogIndex: Boolean?,
        timeZone: TimeZone?
    ) : this(DatabaseBuilder(File(path)).apply {
        ::setFileFormat tryInvoke fileFormat?.let(FILE_FORMAT_MAP.inverse::get)
        ::setReadOnly tryInvoke readOnly
        ::setAutoSync tryInvoke autoSync
        ::setIgnoreBrokenSystemCatalogIndex tryInvoke ignoreBrokenSystemCatalogIndex
        ::setTimeZone tryInvoke timeZone?.toJavaZoneId()?.let(java.util.TimeZone::getTimeZone)
    }.open().apply {
        dateTimeType = DateTimeType.LOCAL_DATE_TIME
    })

    public actual val path: String
        get() = database.path.toString()

    public actual val tableNames: Set<String>
        @Throws(IOException::class)
        get() = database.tableNames

    public actual val systemTableNames: Set<String>
        @Throws(IOException::class)
        get() = database.systemTableNames

    public actual override operator fun iterator(): Iterator<Table> =
        database.iterator().asSequence().map(::Table).iterator()


    @Throws(IOException::class)
    public actual fun getTable(name: String): Table = Table(database.getTable(name))

    @Throws(IOException::class)
    public actual fun getTableMetaData(name: String): TableMetaData =
        TableMetaData(database.getTableMetaData((name)))

    @Throws(IOException::class)
    public actual fun getRelationships(table1: Table, table2: Table): List<Relationship> =
        database.getRelationships(table1.table, table2.table)
            .map(::Relationship)

    @Throws(IOException::class)
    public actual fun getRelationships(table: Table): List<Relationship> =
        database.getRelationships(table.table).map(::Relationship)

    public actual val relationships: List<Relationship>
        get() = database.relationships.map(::Relationship)

    public actual val systemRelationships: List<Relationship>
        @Throws(IOException::class)
        get() = database.systemRelationships.map(::Relationship)

    public actual val queries: List<Query>
        @Throws(IOException::class)
        get() = database.queries.map { query ->
            when (query) {
                is AppendQuery -> JavaAppendQuery(query)
                is CrossTabQuery -> JavaCrossTabQuery(query)
                is DataDefinitionQuery -> JavaDataDefinitionQuery(query)
                is DeleteQuery -> JavaDeleteQuery(query)
                is MakeTableQuery -> JavaMakeTableQuery(query)
                is PassthroughQuery -> JavaPassthroughQuery(query)
                is SelectQuery -> JavaSelectQuery(query)
                is UnionQuery -> JavaUnionQuery(query)
                is UpdateQuery -> JavaUpdateQuery(query)

                else -> error("Unsupported query")
            }

        }

    @Throws(IOException::class)
    public actual fun getSystemTable(tableName: String): Table = Table(database.getSystemTable(tableName))

    public actual val databaseProperties: PropertyMap
        @Throws(IOException::class)
        get() = PropertyMap(database.databaseProperties)

    public actual val summaryProperties: PropertyMap
        @Throws(IOException::class)
        get() = PropertyMap(database.summaryProperties)

    public actual val userDefinedProperties: PropertyMap
        @Throws(IOException::class)
        get() = PropertyMap(database.userDefinedProperties)

    public actual val databasePassword: String?
        @Throws(IOException::class)
        get() = database.databasePassword

    @Throws(IOException::class)
    public actual fun createLinkedTable(name: String, linkedDbName: String, linkedTableName: String): Unit =
        database.createLinkedTable(name, linkedDbName, linkedTableName)

    @Throws(IOException::class)
    public actual fun flush(): Unit = database.flush()

    public actual var errorHandler: ErrorHandler?
        get() = JavaErrorHandler(database.errorHandler)
        set(value) {
            database.errorHandler = value?.toErrorHandler()
        }

    public actual var linkResolver: LinkResolver
        get() = JavaLinkResolver(database.linkResolver)
        set(value) {
            database.linkResolver = value.toLinkResolver()
        }

    @Throws(IOException::class)
    public actual fun isLinkedTable(table: Table): Boolean = database.isLinkedTable(table.table)

    public actual var timeZone: TimeZone
        get() = TimeZone.of(database.timeZone.toZoneId().id)
        set(value) {
            database.timeZone = java.util.TimeZone.getTimeZone(value.toJavaZoneId())
        }

    public actual var charset: Charset
        get() = Charset.valueOf(database.charset.name().replace("-", "_"))
        set(value) {
            database.charset = java.nio.charset.Charset.forName(value.name.replace("_", "-"))
        }

    public actual var columnOrder: ColumnOrder
        get() = COLUMN_ORDER_MAP[database.columnOrder]!!
        set(value) {
            database.columnOrder = COLUMN_ORDER_MAP.inverse[value]
        }

    public actual var isEnforceForeignKeys: Boolean
        get() = database.isEnforceForeignKeys
        set(value) {
            database.isEnforceForeignKeys = value
        }

    public actual var isAllowAutoNumberInsert: Boolean
        get() = database.isAllowAutoNumberInsert
        set(value) {
            database.isAllowAutoNumberInsert = value
        }

    public actual var isEvaluateExpressions: Boolean
        get() = database.isEvaluateExpressions
        set(value) {
            database.isEvaluateExpressions = value
        }

    public actual var columnValidatorFactory: ColumnValidatorFactory?
        get() = JavaColumnValidatorFactory(database.columnValidatorFactory)
        set(value) {
            database.columnValidatorFactory = value?.toColumnValidatorFactory()
        }

    public actual val fileFormat: FileFormat
        @Throws(IOException::class)
        get() = FILE_FORMAT_MAP[database.fileFormat]!!

    public actual override fun close(): Unit = database.close()

    public companion object {
        internal val FILE_FORMAT_MAP = biMapOf(
            Database.FileFormat.V1997 to FileFormat.V1997,
            Database.FileFormat.GENERIC_JET4 to FileFormat.GENERIC_JET4,
            Database.FileFormat.V2000 to FileFormat.V2000,
            Database.FileFormat.V2003 to FileFormat.V2003,
            Database.FileFormat.V2007 to FileFormat.V2007,
            Database.FileFormat.V2010 to FileFormat.V2010,
            Database.FileFormat.V2016 to FileFormat.V2016,
            Database.FileFormat.MSISAM to FileFormat.MSISAM,
        )
    }
}
