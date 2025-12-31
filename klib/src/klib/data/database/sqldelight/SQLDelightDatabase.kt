package klib.data.database.sqldelight

import app.cash.sqldelight.SuspendingTransacterImpl
import app.cash.sqldelight.db.SqlDriver

public class SQLDelightDatabase(
    driver: SqlDriver,
) : SuspendingTransacterImpl(driver) {

    public val queries: SQLDelightQueries = SQLDelightQueries(driver)
}
