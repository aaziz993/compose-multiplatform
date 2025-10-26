package klib.data.cache.room.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["key"], unique = true)],
)
public data class KeyValue(
    @PrimaryKey(autoGenerate = true)
    public val id: Long = 0,
    public val key: String?,
    public val value: String?,
)
