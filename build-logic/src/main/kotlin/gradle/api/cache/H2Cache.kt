package gradle.api.cache

import klib.data.cache.Cache
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

public class H2Cache(databaseFile: File) : Cache<String, String>, AutoCloseable {

    private val connection: Connection =
        DriverManager.getConnection(
            "jdbc:h2:file:${databaseFile.parentFile.apply(File::mkdirs)};AUTO_SERVER=TRUE",
        ).apply {
            createStatement().use { preparedStatement ->
                preparedStatement.executeUpdate(
                    """CREATE TABLE IF NOT EXISTS cache ("key" VARCHAR PRIMARY KEY, "value" VARCHAR)""",
                )
            }
        }

    override fun get(key: String): String? {
        val preparedStatement = connection.prepareStatement("""SELECT "value" FROM cache WHERE "key" = ?""")
        preparedStatement.use { preparedStatement ->
            preparedStatement.setString(1, key)
            val rs = preparedStatement.executeQuery()
            return if (rs.next()) rs.getString(1) else null
        }
    }

    override fun set(key: String, value: String) {
        val preparedStatement =
            connection.prepareStatement("""MERGE INTO cache ("key", "value") KEY("key") VALUES (?, ?)""")
        preparedStatement.use { preparedStatement ->
            preparedStatement.setString(1, key)
            preparedStatement.setString(2, value)
            preparedStatement.executeUpdate()
        }
    }

    override fun remove(key: String) {
        val preparedStatement = connection.prepareStatement("""DELETE FROM cache WHERE "key" = ?""")
        preparedStatement.use { preparedStatement ->
            preparedStatement.setString(1, key)
            preparedStatement.executeUpdate()
        }
    }

    override fun clear() {
        connection.createStatement().use { preparedStatement ->
            preparedStatement.executeUpdate("""DELETE FROM cache""")
        }
    }

    override fun close(): Unit = connection.close()

    override fun asMap(): Map<String, String> = buildMap {
        val preparedStatement = connection.prepareStatement("""SELECT * FROM cache""")
        preparedStatement.use { preparedStatement ->
            val resultSet = preparedStatement.executeQuery()

            while (resultSet.next()) {
                put(resultSet.getString("key"), resultSet.getString("value"))
            }
        }
    }
}
