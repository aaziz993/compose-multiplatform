package klib.data.cache.room.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["key"], unique = true)])
public data class KeyValue(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val key: String?,
    val value: String?,
)
