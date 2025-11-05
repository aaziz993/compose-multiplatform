@file:OptIn(ExperimentalWasmJsInterop::class)

package klib.data.db.sqldelight

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.worker.createDefaultWebWorkerDriver
import kotlin.js.ExperimentalWasmJsInterop

public actual fun deleteSqlDelightDatabase(databaseName: String) {}

public actual fun createSQLDelightDriver(
    schema: SqlSchema<QueryResult.Value<Unit>>,
    databaseName: String
): SqlDriver = createDefaultWebWorkerDriver().also(schema::create)

public actual suspend fun createSQLDelightDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String
): SqlDriver = createDefaultWebWorkerDriver().also { driver -> schema.create(driver).await() }

public actual fun createInMemorySQLDelightDriver(
    schema: SqlSchema<QueryResult.Value<Unit>>,
    databaseName: String
): SqlDriver = createSQLDelightDriver(schema, databaseName)

public actual suspend fun createInMemorySQLDelightDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String
): SqlDriver = createSQLDelightDriver(schema, databaseName)
