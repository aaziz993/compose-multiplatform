package klib.data.db.room

import androidx.room.RoomDatabase

public expect inline fun <reified T : RoomDatabase> createRoomDatabaseBuilder(databaseName: String): RoomDatabase.Builder<T>

public expect inline fun <reified T : RoomDatabase> createInMemoryRoomDatabaseBuilder(): RoomDatabase.Builder<T>
