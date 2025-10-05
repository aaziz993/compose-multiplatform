package klib.data.cache.room.model

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.Index
import androidx.room.PrimaryKey

@Fts4
@Entity(tableName = "cache", indices = [Index(value = ["key"], unique = true)])
public class Cache(
    @PrimaryKey(autoGenerate = true)
    public val id: Long = 0,
    public val key: String,
    public val value: String,
)
