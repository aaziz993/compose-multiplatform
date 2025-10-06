package klib.data.cache.room

import klib.data.cache.room.model.Cache
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
public interface CacheDao {

    @Insert
    public suspend fun insert(cache: Cache)

    @Query("SELECT * FROM Cache WHERE key = :key")
    public suspend fun select(key: String): Cache?

    @Query("SELECT * FROM Cache")
    public suspend fun selectAll(): List<Cache>

    @Query("DELETE FROM Cache WHERE key = :key")
    public suspend fun delete(key: String)

    @Query("DELETE FROM Cache WHERE key LIKE :key")
    public suspend fun deleteLike(key: String)

    @Query("DELETE FROM Cache")
    public suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT 1 FROM Cache WHERE key = :key)")
    public suspend fun exists(key: String): Boolean

    @Query("SELECT COUNT(*) FROM Cache")
    public suspend fun count(): Long
}
