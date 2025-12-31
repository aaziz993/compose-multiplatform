package klib.data.database.sqldelight

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.native.inMemoryDriver
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

public actual fun deleteSqlDelightDatabase(databaseName: String) {
    val fm = NSFileManager.defaultManager

    val documentsPath = fm.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )?.path ?: return

    val dbFile = "$documentsPath/$databaseName"

    if (fm.fileExistsAtPath(dbFile)) fm.removeItemAtPath(dbFile, null)

    // Remove journal files if needed.
    listOf("-wal", "-shm").map(dbFile::plus).forEach { file ->
        if (fm.fileExistsAtPath(file)) fm.removeItemAtPath(file, null)
    }
}

public actual fun createSQLDelightDriver(
    schema: SqlSchema<QueryResult.Value<Unit>>,
    databaseName: String
): SqlDriver = NativeSqliteDriver(schema, databaseName)

public actual suspend fun createSQLDelightDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String
): SqlDriver = createSQLDelightDriver(schema.synchronous(), databaseName)

public actual fun createInMemorySQLDelightDriver(
    schema: SqlSchema<QueryResult.Value<Unit>>,
    databaseName: String
): SqlDriver = inMemoryDriver(schema)

public actual suspend fun createInMemorySQLDelightDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String
): SqlDriver = createInMemorySQLDelightDriver(schema.synchronous(), databaseName)
