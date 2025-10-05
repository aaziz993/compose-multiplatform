package klib.data.cache.source

import com.diamondedge.logging.logging
import klib.data.cache.Cache
import klib.data.cache.Cache4K
import klib.data.cache.source.model.CacheReadPolicy
import klib.data.cache.source.model.CachedSourceResult
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock

/**
 * Caching layer that loads a result from the given source or uses cache.
 */
public class CachedSource<K : Any, V : Any>(
    private val source: (key: K) -> V,
    private val cache: Cache<K, V> = Cache4K(io.github.reactivecircus.cache4k.Cache.Builder<K, V>().build()),
) {

    private val cacheLock = reentrantLock()

    /**
     * Clears underlying cache.
     */
    public fun clearCache(): Unit = cacheLock.withLock {
        cache.clear()
    }

    /**
     * Get or load a result based on given parameters.
     *
     * @param key request parameters.
     * Must be 1) a data-class or 2) primitive or 3) has equals/hash code implemented for proper distinction.
     * Use [Unit] ot [Int] or [CachedSourceNoParams] if there are no parameters for request.
     *
     * @param readPolicy preferred mode of getting from cache.
     *
     * @return Flow that emits 1 (or 2 in case of [CacheReadPolicy.CACHED_THEN_LOAD]) elements or exception.
     */
    public operator fun get(
        key: K,
        readPolicy: CacheReadPolicy = CacheReadPolicy.IF_HAVE,
        update: (Pair<K, CachedSourceResult<V>>) -> Unit = { },
    ): Sequence<V> = getRaw(key, readPolicy, update).map { it.value }

    /**
     * See [get]
     */
    public fun getRaw(
        key: K,
        readPolicy: CacheReadPolicy,
        update: (Pair<K, CachedSourceResult<V>>) -> Unit = { },
    ): Sequence<CachedSourceResult<V>> = when (readPolicy) {

        CacheReadPolicy.NEVER -> {
            getFromSource(key, update)
        }

        CacheReadPolicy.IF_FAILED -> {
            try {
                getFromSource(key, update)
            }
            catch (e: Throwable) {
                sequence {
                    val cached = getFromCache(key)
                    if (cached != null) {
                        yield(
                            CachedSourceResult(
                                cached,
                                true,
                            ),
                        )
                    }
                    else {
                        throw e
                    }
                }
            }
        }

        CacheReadPolicy.IF_HAVE -> {
            val cached = getFromCache(key)
            log.debug { "get IF_HAVE: $key / cached: $cached" }
            if (cached != null) {
                sequenceOf(
                    CachedSourceResult(
                        cached,
                        true,
                    ),
                )
            }
            else {
                getFromSource(key, update)
            }
        }

        CacheReadPolicy.ONLY -> {
            val cached = getFromCache(key)
            if (cached != null) {
                sequenceOf(
                    CachedSourceResult(
                        cached,
                        true,
                    ),
                )
            }
            else {
                sequence { throw NullPointerException("Cache is empty") }
            }
        }

        CacheReadPolicy.CACHED_THEN_LOAD -> {
            val cached = getFromCache(key)
            log.debug { "get FROM_CACHE_THEN_LOAD: $key / cached: $cached" }
            sequence {
                if (cached != null) {
                    yield(
                        CachedSourceResult(
                            cached,
                            true,
                        ),
                    )
                }
                yieldAll(
                    getFromSource(key, update),
                )
            }
        }
    }

    public fun getOrRequest(
        key: K,
        update: (Pair<K, CachedSourceResult<V>>) -> Unit = { },
        predicate: (cached: CachedSourceResult<V>) -> Boolean,
    ): Sequence<V> =
        sequence {
            getRaw(key, CacheReadPolicy.IF_HAVE, update)
                .forEach {
                    if (!it.fromCache || predicate(it)) {
                        yield(it.value)
                    }
                    else {
                        yieldAll(
                            getRaw(key, CacheReadPolicy.NEVER, update)
                                .map { result -> result.value },
                        )
                    }
                }
        }

    private fun getFromSource(
        key: K,
        update: (Pair<K, CachedSourceResult<V>>) -> Unit = { },
    ): Sequence<CachedSourceResult<V>> =
        sequenceOf(source(key))
            .map { CachedSourceResult(it, false) }
            .onEach {
                log.debug { "getFromSource: $key -> $it" }
                putToCache(
                    it.value,
                    key,
                )
                update(key to it)
            }

    private fun getFromCache(key: K): V? = cacheLock.withLock {
        cache[key]
    }

    private fun putToCache(value: V, key: K) = cacheLock.withLock {
        cache[key] = value
    }

    public companion object {

        private val log = logging()
    }
}

@Suppress("FunctionName")
public fun <V : Any> CachedSourceNoParams(
    source: () -> V,
    cache: Cache<Int, V> = Cache4K(io.github.reactivecircus.cache4k.Cache.Builder<Int, V>().build()),
): CachedSource<Int, V> = CachedSource(
    { source() }, cache,
)

public fun <K : Any, V : Any> ((params: K) -> V).cached(
    cache: Cache<K, V> = Cache4K(io.github.reactivecircus.cache4k.Cache.Builder<K, V>().build()),
): CachedSource<K, V> = CachedSource(
    this,
    cache,
)

public fun <V : Any> (() -> V).cached(
    cache: Cache<Int, V> = Cache4K(io.github.reactivecircus.cache4k.Cache.Builder<Int, V>().build()),
): CachedSource<Int, V> = CachedSource(
    { this() },
    cache,
)

public fun <V : Any> CachedSource<Int, V>.get(
    readPolicy: CacheReadPolicy,
): Sequence<V> = get(0, readPolicy)

