package klib.data.cache.room.model

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.Index

// An FTS entity table always has a column named rowid that is the equivalent of an INTEGER PRIMARY KEY index.
// Therefore, an FTS entity can only have a single field annotated with PrimaryKey, it must be named rowid and must be of INTEGER affinity.
// The field can be optionally omitted in the class but can still be used in queries.
@Entity(tableName = "cache", indices = [Index(value = ["key"], unique = true)])
@Fts4
public class Cache(
    public val key: String,
    public val value: String,
)
