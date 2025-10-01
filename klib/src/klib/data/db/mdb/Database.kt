package klib.data.db.mdb

import klib.data.db.mdb.query.Query
import klib.data.type.primitives.string.Charset
import kotlinx.datetime.TimeZone
import kotlinx.io.IOException

/**
 * An Access database instance.  A new instance can be instantiated by opening
 * an existing database file ([DatabaseBuilder.open]) or creating
 * a new database file ([DatabaseBuilder.create]) (for
 * more advanced opening/creating use [DatabaseBuilder]).  Once a
 * Database has been opened, you can interact with the data via the relevant
 * [Table].  When a Database instance is no longer useful, it should
 * **always** be closed ([.close]) to avoid corruption.
 *
 *
 * Database instances (and all the related objects) are *not*
 * thread-safe.  However, separate Database instances (and their respective
 * objects) can be used by separate threads without a problem.
 *
 *
 * Database instances do not implement any "transactional" support, and
 * therefore concurrent editing of the same database file by multiple Database
 * instances (or with outside programs such as MS Access) *will generally
 * result in database file corruption*.
 *
 * @author Aziz Atoev
 * @usage _general_class_
 */
public expect class Database(
    path: String,
    fileFormat: FileFormat? = null,
    readOnly: Boolean? = null,
    autoSync: Boolean? = null,
    ignoreBrokenSystemCatalogIndex: Boolean? = null,
    timeZone: TimeZone? = null,
) : Iterable<Table>, AutoCloseable {
    /**
     * Enum which indicates which version of Access created the database.
     * @usage _general_class_
     */

    /**
     * Returns the File underlying this Database
     */
    public val path: String

    /**
     * @return The names of all of the user tables
     * @usage _general_method_
     */
    // @get:Throws(IOException::class)
    public val tableNames: Set<String>

    /**
     * @return The names of all of the system tables (String).  Note, in order
     * to read these tables, you must use [.getSystemTable].
     * *Extreme care should be taken if modifying these tables
     * directly!*.
     * @usage _intermediate_method_
     */
    // @get:Throws(IOException::class)
    public val systemTableNames: Set<String>

    /**
     * @return an unmodifiable Iterator of the user Tables in this Database.
     * @throws RuntimeIOException if an IOException is thrown by one of the
     * operations, the actual exception will be contained within
     * @throws ConcurrentModificationException if a table is added to the
     * database while an Iterator is in use.
     * @usage _general_method_
     */
    override fun iterator(): Iterator<Table>

    /**
     * @param name User table name (case-insensitive)
     * @return The Table, or null if it doesn't exist (or is a system table)
     * @usage _general_method_
     */
    @Throws(IOException::class)
    public fun getTable(name: String): Table

    /**
     * @param name Table name (case-insensitive), may be any table type
     * (i.e. includes system or linked tables).
     * @return The meta data for the table, or null if it doesn't exist
     * @usage _intermediate_method_
     */
    @Throws(IOException::class)
    public fun getTableMetaData(name: String): TableMetaData

    /**
     * Finds all the relationships in the database between the given tables.
     * @usage _intermediate_method_
     */
    @Throws(IOException::class)
    public fun getRelationships(table1: Table, table2: Table): List<Relationship>

    /**
     * Finds all the relationships in the database for the given table.
     * @usage _intermediate_method_
     */
    @Throws(IOException::class)
    public fun getRelationships(table: Table): List<Relationship>

    /**
     * Finds all the relationships in the database in *non-system* tables.
     *
     *
     * Warning, this may load *all* the Tables (metadata, not data) in the
     * database which could cause memory issues.
     * @usage _intermediate_method_
     */
    // @get:Throws(IOException::class)
    public val relationships: List<Relationship>

    /**
     * Finds *all* the relationships in the database, *including system
     * tables*.
     *
     *
     * Warning, this may load *all* the Tables (metadata, not data) in the
     * database which could cause memory issues.
     * @usage _intermediate_method_
     */
    // @get:Throws(IOException::class)
    public val systemRelationships: List<Relationship>

    /**
     * Finds all the queries in the database.
     * @usage _intermediate_method_
     */
    // @get:Throws(IOException::class)
    public val queries: List<Query>

    /**
     * Returns a reference to *any* available table in this access
     * database, including system tables.
     *
     *
     * Warning, this method is not designed for common use, only for the
     * occassional time when access to a system table is necessary.  Messing
     * with system tables can strip the paint off your house and give your whole
     * family a permanent, orange afro.  You have been warned.
     *
     * @param tableName Table name, may be a system table
     * @return The table, or `null` if it doesn't exist
     * @usage _intermediate_method_
     */
    @Throws(IOException::class)
    public fun getSystemTable(tableName: String): Table

    /**
     * @return the core properties for the database
     * @usage _general_method_
     */
    // @get:Throws(IOException::class)
    public val databaseProperties: PropertyMap

    /**
     * @return the summary properties for the database
     * @usage _general_method_
     */
    // @get:Throws(IOException::class)
    public val summaryProperties: PropertyMap

    /**
     * @return the user-defined properties for the database
     * @usage _general_method_
     */
    // @get:Throws(IOException::class)
    public val userDefinedProperties: PropertyMap

    /**
     * @return the current database password, or `null` if none set.
     * @usage _general_method_
     */
    // @get:Throws(IOException::class)
    public val databasePassword: String?

    /**
     * Create a new table in this database
     * @param name Name of the table to create in this database
     * @param linkedDbName path to the linked database
     * @param linkedTableName name of the table in the linked database
     * @usage _general_method_
     */
    @Throws(IOException::class)
    public fun createLinkedTable(
        name: String, linkedDbName: String,
        linkedTableName: String
    )

    /**
     * Flushes any current changes to the database file (and any linked
     * databases) to disk.
     * @usage _general_method_
     */
    @Throws(IOException::class)
    public fun flush()

    /**
     * Close the database file (and any linked databases).  A Database
     * **must** be closed after use or changes could be lost and the Database
     * file corrupted.  A Database instance should be treated like any other
     * external resource which would be closed in a finally block (e.g. an
     * OutputStream or jdbc Connection).
     * @usage _general_method_
     */
    override fun close()

    /**
     * The currently configured ErrorHandler (always non-`null`).
     * This will be used to handle all errors unless overridden at the Table or
     * Cursor level.
     * @usage _intermediate_method_
     */
    public var errorHandler: ErrorHandler?

    /**
     * The currently configured LinkResolver (always non-`null`).
     * This will be used to handle all linked database loading.
     * @usage _intermediate_method_
     */
    public var linkResolver: LinkResolver


    /**
     * Returns `true` if this Database links to the given Table, `false` otherwise.
     * @usage _general_method_
     */
    @Throws(IOException::class)
    public fun isLinkedTable(table: Table): Boolean

    /**
     * Currently configured TimeZone (always non-`null` and aligned
     * with the ZoneId).
     * @usage _intermediate_method_
     */
    public var timeZone: TimeZone

    /**
     * Gets currently configured Charset (always non-`null`).
     * @usage _intermediate_method_
     */
    public var charset: Charset

    /**
     * Currently configured [Table.ColumnOrder] (always non-`null`).
     * @usage _intermediate_method_
     */
    public var columnOrder: ColumnOrder

    /**
     * Current foreign-key enforcement policy.
     * @usage _intermediate_method_
     */
    public var isEnforceForeignKeys: Boolean

    /**
     * Current allow auto number insert policy.  By default, jackcess does
     * not allow auto numbers to be inserted or updated directly (they are
     * always handled internally by the Table).  Setting this policy to `true` allows the caller to optionally set the value explicitly when
     * adding or updating rows (if a value is not provided, it will still be
     * handled internally by the Table).  This value can be set database-wide
     * using [.setAllowAutoNumberInsert] and/or on a per-table basis using
     * [Table.setAllowAutoNumberInsert] (and/or on a jvm-wide using the
     * [.ALLOW_AUTONUM_INSERT_PROPERTY] system property).  Note that
     * *enabling this feature should be done with care* to reduce the
     * chances of screwing up the database.
     *
     * @usage _intermediate_method_
     */
    public var isAllowAutoNumberInsert: Boolean

    /**
     * The current expression evaluation policy.  Expression evaluation is
     * enabled by default but can be disabled if necessary.
     */
    public var isEvaluateExpressions: Boolean

    /**
     * ColumnValidatorFactory.  If `null`, resets to the
     * default value.  The configured ColumnValidatorFactory will be used to
     * create ColumnValidator instances on any *user* tables loaded from
     * this point onward (this will not be used for system tables).
     * @usage _intermediate_method_
     */
    public var columnValidatorFactory: ColumnValidatorFactory?

    /**
     * Returns the FileFormat of this database (which may involve inspecting the
     * database itself).
     * @throws IllegalStateException if the file format cannot be determined
     * @usage _general_method_
     */
    // @get:Throws(IOException::class)
    public val fileFormat: FileFormat

    /**
     * Returns the EvalConfig for configuring expression evaluation.
     */
//    public val evalConfig: EvalConfig
}

public enum class FileFormat(public val fileExtension: String) {
    /** A database which was created by MS Access 97  */
    V1997(".mdb"),

    /** A database which was most likely created programmatically (e.g. using
     * windows ADOX)  */
    GENERIC_JET4(".mdb"),

    /** A database which was created by MS Access 2000  */
    V2000(".mdb"),

    /** A database which was created by MS Access 2002/2003  */
    V2003(".mdb"),

    /** A database which was created by MS Access 2007  */
    V2007(".accdb"),

    /** A database which was created by MS Access 2010+  */
    V2010(".accdb"),

    /** A database which was created by MS Access 2016+  */
    V2016(".accdb"),

    /** A database which was created by MS Access 2019+ (Office 365)  */
    V2019(".accdb"),

    /** A database which was created by MS Money  */
    MSISAM(".mny");
}
