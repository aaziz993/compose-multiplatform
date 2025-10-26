package klib.data.db.sqldelight

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import splitties.init.appCtx

public actual fun createSQLDelightDriver(
    schema: SqlSchema<QueryResult.Value<Unit>>,
    databaseName: String
): SqlDriver = AndroidSqliteDriver(schema, appCtx, "$databaseName.db")

public actual suspend fun createSQLDelightDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String
): SqlDriver = createSQLDelightDriver(schema.synchronous(), databaseName)

public actual fun createInMemorySQLDelightDriver(
    schema: SqlSchema<QueryResult.Value<Unit>>,
    databaseName: String
): SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also(schema::create)

public actual suspend fun createInMemorySQLDelightDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String
): SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also { schema.create(it).await() }
