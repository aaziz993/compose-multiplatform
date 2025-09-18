package klib.data.cache

import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

public abstract class JdbcCache<K, V>(
    dbFile: File,
    private val keySerializer: KSerializer<K>,
    private val valueSerializer: KSerializer<V>,
    private val json: Json = Json { ignoreUnknownKeys = true }
) : Cache<K, V>, AutoCloseable {

    protected val connection: Connection = DriverManager.getConnection(jdbcUrl(dbFile)).apply {
        createStatement().use { stmt ->
            stmt.executeUpdate(createTableSql())
        }
    }

    /** Subclasses must provide the JDBC URL */
    protected abstract fun jdbcUrl(dbFile: File): String

    /** Subclasses provide dialect-specific CREATE TABLE */
    protected abstract fun createTableSql(): String

    /** Subclasses provide dialect-specific UPSERT */
    protected abstract fun upsertSql(): String

    override fun get(key: K): V? =
        connection.prepareStatement("""SELECT "value" FROM cache WHERE "key" = ?""").use { ps ->
            ps.setString(1, json.encodeToString(keySerializer, key))
            ps.executeQuery().use { rs ->
                if (rs.next()) json.decodeFromString(valueSerializer, rs.getString(1)) else null
            }
        }

    override fun set(key: K, value: V): Unit = connection.prepareStatement(upsertSql()).use { ps ->
        ps.setString(1, json.encodeToString(keySerializer, key))
        ps.setString(2, json.encodeToString(valueSerializer, value))
        ps.executeUpdate()
    }

    override fun remove(key: K): Unit =
        connection.prepareStatement("""DELETE FROM cache WHERE "key" = ?""").use { ps ->
            ps.setString(1, json.encodeToString(keySerializer, key))
            ps.executeUpdate()
        }

    override fun clear(): Unit = connection.createStatement().use { stmt ->
        stmt.executeUpdate("""DELETE FROM cache""")
    }

    override fun asMap(): Map<K, V> = buildMap {
        connection.prepareStatement("""SELECT "key","value" FROM cache""").use { ps ->
            val rs = ps.executeQuery()

            while (rs.next()) {
                put(
                    json.decodeFromString(keySerializer, rs.getString("key")),
                    json.decodeFromString(valueSerializer, rs.getString("value"))
                )
            }
        }
    }

    override fun close(): Unit = connection.close()
}
