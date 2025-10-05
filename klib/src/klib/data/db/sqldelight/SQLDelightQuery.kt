package klib.data.db.sqldelight

import app.cash.sqldelight.Query
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver

internal class SQLDelightQuery<out T : Any>(
    private val driver: SqlDriver,
    private val tableName: String,
    private val sql: String,
    private val parameters: List<Any?>,
    private val identifier: Int?,
    mapper: (SqlCursor) -> T
) : Query<T>(mapper) {

    override fun addListener(listener: Listener): Unit = driver.addListener(tableName, listener = listener)

    override fun removeListener(listener: Listener): Unit = driver.removeListener(tableName, listener = listener)

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(
            identifier,
            sql,
            mapper,
            parameters.size,
        ) {
            parameters.forEachIndexed { index, parameter ->
                when (parameter) {
                    is Boolean -> bindBoolean(index, parameter)
                    is Long -> bindLong(index, parameter)
                    is Double -> bindDouble(index, parameter)
                    is String -> bindString(index, parameter)
                    is ByteArray -> bindBytes(index, parameter)
                    else -> throw IllegalArgumentException("Unsupported parameter type ${parameter?.let { it::class.simpleName }}")
                }
            }
        }

    override fun toString(): String = "$tableName.sq:$sql"
}
