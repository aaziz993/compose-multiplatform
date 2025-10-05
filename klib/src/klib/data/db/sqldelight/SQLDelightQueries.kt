package klib.data.db.sqldelight

import app.cash.sqldelight.Query
import app.cash.sqldelight.SuspendingTransacterImpl
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver

public class SQLDelightQueries(
    driver: SqlDriver,
) : SuspendingTransacterImpl(driver) {

    public fun <T : Any> query(
        tableName: String,
        sql: String,
        parameters: List<Any?> = emptyList(),
        identifier: Int? = null,
        mapper: (SqlCursor) -> T
    ): Query<T> = SQLDelightQuery(
        driver,
        tableName,
        sql,
        parameters,
        identifier,
        mapper,
    )
}
