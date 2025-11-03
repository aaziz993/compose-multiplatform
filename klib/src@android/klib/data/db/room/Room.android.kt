package klib.data.db.room

import android.annotation.SuppressLint
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.util.findAndInstantiateDatabaseImpl
import splitties.init.appCtx

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
