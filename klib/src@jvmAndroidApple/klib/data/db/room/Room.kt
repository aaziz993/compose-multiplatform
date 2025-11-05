package klib.data.db.room

import androidx.room.RoomDatabase

public expect fun deleteRoomDatabase(databaseName: String)

public expect inline fun <reified T : RoomDatabase> createRoomDatabaseBuilder(
    databaseName: String,
    noinline factory: (() -> T)? = null,
): RoomDatabase.Builder<T>

public expect inline fun <reified T : RoomDatabase> createInMemoryRoomDatabaseBuilder(
    noinline factory: (() -> T)? = null,
): RoomDatabase.Builder<T>
