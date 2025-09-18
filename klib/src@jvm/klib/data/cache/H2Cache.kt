package klib.data.cache

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import kotlin.use

public class H2Cache<K, V>(
    dbFile: File,
    keySerializer: KSerializer<K>,
    valueSerializer: KSerializer<V>,
    json: Json = Json { ignoreUnknownKeys = true }
) : JdbcCache<K, V>(dbFile, keySerializer, valueSerializer, json) {
    override fun jdbcUrl(dbFile: File): String {
        dbFile.parentFile.mkdirs()
        return "jdbc:h2:file:${dbFile.absolutePath};AUTO_SERVER=TRUE"
    }

    override fun createTableSql(): String =
        """CREATE TABLE IF NOT EXISTS cache ("key" VARCHAR PRIMARY KEY, "value" VARCHAR)"""

    override fun upsertSql(): String =
        """MERGE INTO cache ("key", "value") KEY("key") VALUES (?, ?)"""
}
