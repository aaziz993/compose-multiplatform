package klib.data.cache.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
public data class Cache(
    @PrimaryKey(autoGenerate = true)
    public val id: Long = 0,
    public val key: String?,
    public val value: String?,
)
