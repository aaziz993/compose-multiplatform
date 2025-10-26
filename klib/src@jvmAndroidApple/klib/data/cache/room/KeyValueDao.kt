package klib.data.cache.room

import klib.data.cache.room.model.KeyValue
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
public interface KeyValueDao {

    @Query("SELECT * FROM KeyValue WHERE key = :key")
    public suspend fun selectByKeyNotNull(key: String): KeyValue?

    @Query("SELECT * FROM KeyValue WHERE key IS NULL")
    public suspend fun selectKeyIsNull(): KeyValue?

    public suspend fun selectByKey(key: String?): KeyValue? =
        if (key == null) selectKeyIsNull() else selectByKeyNotNull(key)

    @Query("SELECT * FROM KeyValue")
    public suspend fun selectAll(): List<KeyValue>

    @Query("SELECT EXISTS(SELECT 1 FROM KeyValue WHERE key = :key)")
    public suspend fun existsByKeyNotNull(key: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM KeyValue WHERE key IS NULL)")
    public suspend fun existsKeyIsNull(): Boolean

    public suspend fun existsKey(key: String?): Boolean =
        if (key == null) existsKeyIsNull() else existsByKeyNotNull(key)

    @Query("SELECT COUNT(*) FROM KeyValue")
    public suspend fun count(): Long

    @Insert
    public suspend fun insert(cache: KeyValue)

    @Query("DELETE FROM KeyValue WHERE key = :key")
    public suspend fun deleteByKeyNotNull(key: String)

    @Query("DELETE FROM KeyValue WHERE key IS NULL")
    public suspend fun deleteKeyIsNull()

    public suspend fun deleteByKey(key: String?): Unit =
        if (key == null) deleteKeyIsNull() else deleteByKeyNotNull(key)

    @Query("DELETE FROM KeyValue")
    public suspend fun deleteAll()
}
