package klib.data.database.sqldelight

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema

public expect fun deleteSqlDelightDatabase(databaseName: String)

public expect fun createSQLDelightDriver(
    schema: SqlSchema<QueryResult.Value<Unit>>,
    databaseName: String
): SqlDriver

public expect suspend fun createSQLDelightDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String
): SqlDriver

public expect fun createInMemorySQLDelightDriver(
    schema: SqlSchema<QueryResult.Value<Unit>>,
    databaseName: String
): SqlDriver

public expect suspend fun createInMemorySQLDelightDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String
): SqlDriver

public fun createSQLDelightDatabase(
    schema: SqlSchema<QueryResult.Value<Unit>>,
    databaseName: String,
): SQLDelightDatabase = SQLDelightDatabase(createSQLDelightDriver(schema, databaseName))

public fun createInMemorySQLDelightDatabase(
    schema: SqlSchema<QueryResult.Value<Unit>>,
    databaseName: String,
): SQLDelightDatabase = SQLDelightDatabase(createSQLDelightDriver(schema, databaseName))

public suspend fun createSQLDelightDatabase(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String,
): SQLDelightDatabase = SQLDelightDatabase(createSQLDelightDriver(schema, databaseName))

public suspend fun createInMemorySQLDelightDatabase(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String,
): SQLDelightDatabase = SQLDelightDatabase(createSQLDelightDriver(schema, databaseName))

public suspend fun createSQLDelightKlibDatabase(): KlibDatabase =
    KlibDatabase(createSQLDelightDriver(KlibDatabase.Schema, "KlibDatabase"))

public suspend fun createInMemorySQLDelightKlibDatabase(): KlibDatabase =
    KlibDatabase(createInMemorySQLDelightDriver(KlibDatabase.Schema, "KlibDatabase"))
