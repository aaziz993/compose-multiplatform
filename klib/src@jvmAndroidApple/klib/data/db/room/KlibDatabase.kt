package klib.data.db.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import klib.data.cache.room.KeyValueDao
import klib.data.cache.room.model.KeyValue
import klib.data.coroutines.StandardDispatchers

@Database(entities = [KeyValue::class], version = 1)
@ConstructedBy(KlibDatabaseConstructor::class)
public abstract class KlibDatabase : RoomDatabase() {

    public abstract fun getDao(): KeyValueDao

    public companion object Companion {

        public val callback: Callback = object : Callback() {
            override fun onCreate(connection: SQLiteConnection) {
                super.onCreate(connection)
                // Enforce "only one NULL key" rule.
                connection.execSQL(
                    """
                    CREATE TRIGGER IF NOT EXISTS unique_null_key
                    BEFORE INSERT ON KeyValue
                    WHEN NEW.key IS NULL
                    BEGIN
                        DELETE FROM KeyValue WHERE key IS NULL;
                    END;
                    """.trimIndent(),
                )
            }
        }
    }
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
public expect object KlibDatabaseConstructor : RoomDatabaseConstructor<KlibDatabase> {

    override fun initialize(): KlibDatabase
}

public fun createRoomKlibDatabaseBuilder(): RoomDatabase.Builder<KlibDatabase> =
    createRoomDatabaseBuilder<KlibDatabase>("KlibDatabase")
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(StandardDispatchers.io)
        .addCallback(KlibDatabase.callback)

public fun createInMemoryRoomKlibDatabaseBuilder(): RoomDatabase.Builder<KlibDatabase> =
    createInMemoryRoomDatabaseBuilder<KlibDatabase>()
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(StandardDispatchers.io)
        .addCallback(KlibDatabase.callback)
