package klib.data.cache.source.model

public enum class CacheReadPolicy {

    NEVER,
    IF_FAILED,
    IF_HAVE,
    ONLY,
    CACHED_THEN_LOAD,
}
