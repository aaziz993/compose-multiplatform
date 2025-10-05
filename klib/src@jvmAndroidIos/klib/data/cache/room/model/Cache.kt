package klib.data.cache.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "cache", indices = [Index(value = ["key"], unique = true)])
@Fts4
public class Cache(
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    public val rowId: Int,
    public val key: String,
    public val value: String,
)
