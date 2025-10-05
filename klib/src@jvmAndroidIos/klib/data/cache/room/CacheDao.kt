package klib.data.cache.room

import klib.data.cache.room.model.Cache
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
public interface CacheDao {

    @Insert
    public suspend fun insert(cache: Cache)

    @Query("SELECT rowid, key, value FROM cache WHERE cache MATCH :key")
    public suspend fun search(key: String): List<Cache>

    @Query("SELECT * FROM cache WHERE key = :key")
    public suspend fun select(key: String): Cache?

    @Query("SELECT * FROM cache")
    public suspend fun selectAll(): List<Cache>

    @Query("DELETE FROM cache WHERE key = :key")
    public suspend fun delete(key: String)

    @Query("DELETE FROM cache WHERE key LIKE :key")
    public suspend fun deleteLike(key: String)

    @Query("DELETE FROM cache WHERE cache MATCH :key")
    public suspend fun deleteMatch(key: String)

    @Query("DELETE FROM cache")
    public suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT 1 FROM cache WHERE key = :key)")
    public suspend fun exists(key: String): Boolean

    @Query("SELECT COUNT(*) FROM cache")
    public suspend fun count(): Long
}
