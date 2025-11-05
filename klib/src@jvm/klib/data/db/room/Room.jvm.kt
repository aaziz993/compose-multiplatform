package klib.data.db.room

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.util.findAndInstantiateDatabaseImpl
import java.io.File

public actual fun deleteRoomDatabase(databaseName: String) {
    val dbFile = File(System.getProperty("java.io.tmpdir"), databaseName)
    if (dbFile.exists()) dbFile.delete()

    // Remove journal files if needed.
    listOf("-shm", "-wal").map { suffix -> File(dbFile.path + suffix) }.forEach(File::delete)
}

public actual inline fun <reified T : RoomDatabase> createRoomDatabaseBuilder(
    databaseName: String,
    noinline factory: (() -> T)?,
): RoomDatabase.Builder<T> = Room.databaseBuilder(
    File(System.getProperty("java.io.tmpdir"), databaseName).absolutePath,
    factory ?: { findAndInstantiateDatabaseImpl(T::class.java) },
)

public actual inline fun <reified T : RoomDatabase> createInMemoryRoomDatabaseBuilder(
    noinline factory: (() -> T)?,
): RoomDatabase.Builder<T> =
    Room.inMemoryDatabaseBuilder(factory ?: { findAndInstantiateDatabaseImpl(T::class.java) })
