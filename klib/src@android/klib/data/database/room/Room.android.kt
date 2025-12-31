package klib.data.database.room

import android.annotation.SuppressLint
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.util.findAndInstantiateDatabaseImpl
import java.io.File
import splitties.init.appCtx

public actual fun deleteRoomDatabase(databaseName: String) {
    val dbFile = appCtx.getDatabasePath(databaseName)
    if (dbFile.exists()) dbFile.delete()

    // Remove journal files if needed.
    listOf("-shm", "-wal").map { suffix -> File(dbFile.path + suffix) }.forEach(File::delete)
}

@SuppressLint("RestrictedApi")
public actual inline fun <reified T : RoomDatabase> createRoomDatabaseBuilder(
    databaseName: String,
    noinline factory: (() -> T)?,
): RoomDatabase.Builder<T> = Room.databaseBuilder(
    appCtx,
    appCtx.getDatabasePath(databaseName).absolutePath,
    factory ?: { findAndInstantiateDatabaseImpl(T::class.java) },
)

@SuppressLint("RestrictedApi")
public actual inline fun <reified T : RoomDatabase> createInMemoryRoomDatabaseBuilder(
    noinline factory: (() -> T)?,
): RoomDatabase.Builder<T> =
    Room.inMemoryDatabaseBuilder(
        appCtx,
        factory ?: { findAndInstantiateDatabaseImpl(T::class.java) },
    )
