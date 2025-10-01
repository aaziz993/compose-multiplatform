package klib.data.db.sqldelight

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema

public expect suspend fun createSQLDelightDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String
): SqlDriver

public expect suspend fun createInMemorySQLDelightDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String
): SqlDriver

public suspend fun createSQLDelightCacheDatabase(databaseName: String = "cache"): Cache =
    Cache(createSQLDelightDriver(Cache.Schema, databaseName))

public suspend fun createInMemorySQLDelightCacheDatabase(databaseName: String = "inMemoryCache"): Cache =
    Cache(createInMemorySQLDelightDriver(Cache.Schema, databaseName))
