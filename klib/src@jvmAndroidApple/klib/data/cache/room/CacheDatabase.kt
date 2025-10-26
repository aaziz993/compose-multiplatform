package klib.data.cache.room

import klib.data.cache.room.model.Cache
import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import klib.data.db.room.createInMemoryRoomDatabaseBuilder
import klib.data.db.room.createRoomDatabaseBuilder

@Database(entities = [Cache::class], version = 1)
@ConstructedBy(CacheDatabaseConstructor::class)
public abstract class CacheDatabase : RoomDatabase() {

    public abstract fun getDao(): CacheDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
public expect object CacheDatabaseConstructor : RoomDatabaseConstructor<CacheDatabase> {

    override fun initialize(): CacheDatabase
}

public fun createRoomCacheDatabaseBuilder(databaseName: String): RoomDatabase.Builder<CacheDatabase> =
    createRoomDatabaseBuilder(databaseName)

public fun createInMemoryRoomCacheDatabaseBuilder(): RoomDatabase.Builder<CacheDatabase> =
    createInMemoryRoomDatabaseBuilder()
