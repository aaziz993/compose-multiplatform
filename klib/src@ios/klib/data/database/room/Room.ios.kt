package klib.data.database.room

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.util.findDatabaseConstructorAndInitDatabaseImpl
import platform.Foundation.NSFileManager
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSUserDomainMask

public actual fun deleteRoomDatabase(databaseName: String) {
    val documentsPath = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )?.path ?: return

    val dbFile = "$documentsPath/$databaseName"
    val fm = NSFileManager.defaultManager
    if (fm.fileExistsAtPath(dbFile)) fm.removeItemAtPath(dbFile, null)
}

public actual inline fun <reified T : RoomDatabase> createRoomDatabaseBuilder(
    databaseName: String,
    noinline factory: (() -> T)?,
): RoomDatabase.Builder<T> = Room.databaseBuilder(
    requireNotNull(
        NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )?.path,
    ) + "/$databaseName",
    factory ?: { findDatabaseConstructorAndInitDatabaseImpl(T::class) },
)

public actual inline fun <reified T : RoomDatabase> createInMemoryRoomDatabaseBuilder(
    noinline factory: (() -> T)?,
): RoomDatabase.Builder<T> =
    Room.inMemoryDatabaseBuilder(factory ?: { findDatabaseConstructorAndInitDatabaseImpl(T::class) })
