package klib.data.cache.source.model

public data class CachedSourceResult<T : Any>(
    val value: T,
    val fromCache: Boolean,
)
