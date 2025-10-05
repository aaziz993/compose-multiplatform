package klib.data.cache.source

import com.diamondedge.logging.logging
import klib.data.cache.CoroutineCache
import klib.data.cache.source.model.CacheReadPolicy
import klib.data.cache.source.model.CachedSourceResult
import io.github.reactivecircus.cache4k.Cache
import klib.data.cache.Cache4K
import klib.data.cache.asCoroutineCache
import klib.data.coroutines.StandardDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Caching layer that loads a result from the given source or uses cache.
 */
public class CoroutineCachedSource<K : Any, V : Any>(
    source: suspend (key: K) -> V,
    private val dispatcher: CoroutineDispatcher = StandardDispatchers.io,
    private val cache: CoroutineCache<K, V> = Cache4K(Cache.Builder<K, V>().build()).asCoroutineCache(dispatcher),
) {

    private val _updates = MutableSharedFlow<Pair<K, CachedSourceResult<V>>>()

    /**
     * Flow of every update from the source.
     * Can be used to observe all future updates.
     * Note: Order of emission is not guaranteed in high concurrency.
     */
    public val updates: SharedFlow<Pair<K, CachedSourceResult<V>>> = _updates

    private val _errors = MutableSharedFlow<Pair<K, Throwable>>()

    /**
     * Flow of every exception from the source.
     * Can be used to observe all future exceptions.
     * Note: Order of emission is not guaranteed in high concurrency.
     */
    public val errors: SharedFlow<Pair<K, Throwable>> = _errors

    private val requester = CoroutineRequester(source)
    private val cacheLock = Mutex()

    /**
     * Clears underlying cache.
     */
    public suspend fun clearCache() {
        cacheLock.withLock {
            cache.clear()
        }
    }

    /**
     * Get or load a result based on given parameters.
     *
     * @param key request parameters, also may be used as key for cache and ongoing sharing.
     * Must be 1) a data-class or 2) primitive or 3) has equals/hash code implemented for proper distinction.
     * Use [Unit] ot [Int] or [CachedSourceNoParams] if there are no parameters for request.
     *
     * @param readPolicy preferred mode of getting from cache.
     *
     * @param shareOngoingRequest allows to share ongoing source request without running in parallel.
     *
     * @return Flow that emits 1 (or 2 in case of [CacheReadPolicy.CACHED_THEN_LOAD]) elements or exception.
     */
    public operator fun get(
        key: K,
        readPolicy: CacheReadPolicy = CacheReadPolicy.IF_HAVE,
        shareOngoingRequest: Boolean = true,
    ): Flow<V> =
        getRaw(key, readPolicy, shareOngoingRequest)
            .map { it.value }

    /**
     * See [get]
     */
    public fun getRaw(
        key: K,
        readPolicy: CacheReadPolicy,
        shareOngoingRequest: Boolean = true,
    ): Flow<CachedSourceResult<V>> {
        val lazyFlow = suspend {
            when (readPolicy) {

                CacheReadPolicy.NEVER -> {
                    getFromSource(key, shareOngoingRequest)
                }

                CacheReadPolicy.IF_FAILED -> {
                    getFromSource(key, shareOngoingRequest)
                        .catch {
                            val cached = getFromCache(key)
                            if (cached != null) {
                                emit(
                                    CachedSourceResult(
                                        cached,
                                        true,
                                    ),
                                )
                            }
                            else {
                                throw it
                            }
                        }
                }

                CacheReadPolicy.IF_HAVE -> {
                    val cached = getFromCache(key)
                    log.debug { "get IF_HAVE: $key / cached: $cached" }
                    if (cached != null) {
                        flow {
                            emit(
                                CachedSourceResult(
                                    cached,
                                    true,
                                ),
                            )
                        }
                    }
                    else {
                        getFromSource(key, shareOngoingRequest)
                    }
                }

                CacheReadPolicy.ONLY -> {
                    val cached = getFromCache(key)
                    if (cached != null) {
                        flow {
                            emit(
                                CachedSourceResult(
                                    cached,
                                    true,
                                ),
                            )
                        }
                    }
                    else {
                        flow { throw NullPointerException("Cache is empty") }
                    }
                }

                CacheReadPolicy.CACHED_THEN_LOAD -> {
                    val cached = getFromCache(key)
                    log.debug { "get FROM_CACHE_THEN_LOAD: $key / cached: $cached" }
                    flow {
                        if (cached != null) {
                            emitAll(
                                flowOf(
                                    CachedSourceResult(
                                        cached,
                                        true,
                                    ),
                                ),
                            )
                        }
                        emitAll(
                            getFromSource(key, shareOngoingRequest),
                        )
                    }
                }
            }
        }
        return flow {
            emitAll(lazyFlow())
        }
    }

    public fun getOrRequest(
        key: K,
        predicate: (cached: CachedSourceResult<V>) -> Boolean,
    ): Flow<V> =
        flow {
            getRaw(key, CacheReadPolicy.IF_HAVE)
                .collect {
                    if (!it.fromCache || predicate(it)) {
                        emit(it.value)
                    }
                    else {
                        emitAll(
                            getRaw(key, CacheReadPolicy.NEVER)
                                .map { result -> result.value },
                        )
                    }
                }
        }

    public fun observeAndRequest(
        key: K,
        requestRetryCount: Long = 2,
    ): Flow<V> =
        flow {
            emitAll(
                merge(
                    updates
                        .filter { it.first == key }
                        .map { it.second.value },

                    get(key, CacheReadPolicy.NEVER)
                        .retry(requestRetryCount)
                        .take(1)
                        // Don't emit from this stream because all updates are emitted from .updates anyway.
                        // The downside: a value from .updates (from another parallel request)
                        // can arrive before this request.
                        // More complex solution is needed to solve this behaviour.
                        .filter { false },
                ),
            )
        }

    public suspend fun getOngoingSize(): Int =
        requester.getOngoingSize()

    private fun getFromSource(
        key: K,
        shareOngoing: Boolean
    ): Flow<CachedSourceResult<V>> =
        when {
            shareOngoing -> requester.requestShared(key, dispatcher)
            else -> requester.request(key, dispatcher)
        }
            .map { CachedSourceResult(it, false) }
            .onEach {
                log.debug { "getFromSource: $key -> $it" }
                putToCache(
                    it.value,
                    key,
                )
                _updates.emit(key to it)
            }
            .catch {
                _errors.emit(key to it)
                throw it
            }

    private suspend fun getFromCache(key: K): V? = cacheLock.withLock {
        cache[key]
    }

    private suspend fun putToCache(value: V, key: K) = cacheLock.withLock {
        cache[key] = value
    }

    public companion object {

        private val log = logging()
    }
}

@Suppress("FunctionName")
public fun <V : Any> CachedSourceNoParams(
    source: suspend () -> V,
    dispatcher: CoroutineDispatcher = StandardDispatchers.io,
    cache: CoroutineCache<Int, V> = Cache4K(Cache.Builder<Int, V>().build()).asCoroutineCache(dispatcher),
): CoroutineCachedSource<Int, V> = CoroutineCachedSource(
    { source() },
    dispatcher,
    cache,
)

public fun <K : Any, V : Any> (suspend (params: K) -> V).cached(
    dispatcher: CoroutineDispatcher = StandardDispatchers.io,
    cache: CoroutineCache<K, V> = Cache4K(Cache.Builder<K, V>().build()).asCoroutineCache(dispatcher),
): CoroutineCachedSource<K, V> = CoroutineCachedSource(
    this,
    dispatcher,
    cache,
)

public fun <V : Any> (suspend () -> V).cached(
    dispatcher: CoroutineDispatcher = StandardDispatchers.io,
    cache: CoroutineCache<Int, V> = Cache4K(Cache.Builder<Int, V>().build()).asCoroutineCache(dispatcher),
): CoroutineCachedSource<Int, V> = CoroutineCachedSource(
    { this() },
    dispatcher,
    cache,
)

public fun <V : Any> CoroutineCachedSource<Int, V>.get(
    readPolicy: CacheReadPolicy,
    shareOngoingRequest: Boolean = true,
): Flow<V> = get(
    0,
    readPolicy,
    shareOngoingRequest,
)


