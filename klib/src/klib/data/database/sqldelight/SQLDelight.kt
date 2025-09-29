package klib.data.database.sqldelight

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import data.database.sqldelight.Cache

public expect suspend fun createSQLDelightDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String
): SqlDriver

public expect suspend fun createInMemorySQLDelightDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String
): SqlDriver

public suspend fun createSQLDelightKeyValueDatabase(databaseName: String = "key_value"): Cache =
    Cache(createSQLDelightDriver(Cache.Schema, databaseName))

public suspend fun createInMemorySQLDelightKeyValueDatabase(databaseName: String = "in_memory_key_value"): Cache =
    Cache(createInMemorySQLDelightDriver(Cache.Schema, databaseName))
