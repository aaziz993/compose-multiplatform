@file:OptIn(ExperimentalForeignApi::class)
package klib.data.db.room

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSUserDomainMask

public actual inline fun <reified T : RoomDatabase> createRoomDatabaseBuilder(
    databaseName: String
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
)

public actual inline fun <reified T : RoomDatabase> createInMemoryRoomDatabaseBuilder(): RoomDatabase.Builder<T> =
    Room.inMemoryDatabaseBuilder()
