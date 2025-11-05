package klib.data.db.sqldelight

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlSchema
import java.io.File

public actual fun deleteSqlDelightDatabase(databaseName: String) {
    val dbFile = File(System.getProperty("java.io.tmpdir"), databaseName)
    if (dbFile.exists()) dbFile.delete()

    // Remove journal files if needed.
    listOf("-shm", "-wal").map { suffix -> File(dbFile.path + suffix) }.forEach(File::delete)
}

public actual fun createSQLDelightDriver(
    schema: SqlSchema<QueryResult.Value<Unit>>,
    databaseName: String
): SqlDriver =
    JdbcSqliteDriver(url = "jdbc:sqlite:${File(System.getProperty("java.io.tmpdir"), databaseName).path}")
        .also(schema::create)

public actual suspend fun createSQLDelightDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String
): SqlDriver =
    JdbcSqliteDriver(url = "jdbc:sqlite:${File(System.getProperty("java.io.tmpdir"), databaseName).path}")
        .also { driver -> schema.create(driver).await() }

public actual fun createInMemorySQLDelightDriver(
    schema: SqlSchema<QueryResult.Value<Unit>>,
    databaseName: String
): SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also(schema::create)

public actual suspend fun createInMemorySQLDelightDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String
): SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also { schema.create(it).await() }
