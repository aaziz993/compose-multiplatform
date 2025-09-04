package gradle.api.cache

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.io.File

public class SqliteCache<K, V>(
    dbFile: File,
    keySerializer: KSerializer<K>,
    valueSerializer: KSerializer<V>,
    json: Json = Json { ignoreUnknownKeys = true }
) : JdbcCache<K, V>(dbFile, keySerializer, valueSerializer, json) {

    override fun jdbcUrl(dbFile: File): String {
        dbFile.parentFile?.mkdirs()
        return "jdbc:sqlite:${dbFile.absolutePath}"
    }

    override fun createTableSql(): String =
        """CREATE TABLE IF NOT EXISTS cache ("key" TEXT PRIMARY KEY, "value" TEXT NOT NULL)"""

    override fun upsertSql(): String =
        """INSERT OR REPLACE INTO cache ("key", "value") VALUES (?, ?)"""
}