package klib.data.db.sqldelight

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.native.inMemoryDriver

public actual fun createSQLDelightDriver(
    schema: SqlSchema<QueryResult.Value<Unit>>,
    databaseName: String
): SqlDriver = NativeSqliteDriver(schema, "$databaseName.db")

public actual suspend fun createSQLDelightDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String
): SqlDriver = createSQLDelightDriver(schema.synchronous(), "$databaseName.db")

public actual suspend fun createInMemorySQLDelightDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String
): SqlDriver = inMemoryDriver(schema.synchronous())
