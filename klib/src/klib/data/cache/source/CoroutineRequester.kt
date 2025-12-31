package klib.data.cache.source

import arrow.atomic.AtomicBoolean
import com.diamondedge.logging.logging
import klib.coroutines.StandardDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

public class CoroutineRequester<K : Any, V : Any>(
    private val source: suspend (key: K) -> V,
) {

    private val ongoings = mutableMapOf<K, Flow<V>>()
    private val ongoingsLock = Mutex()

    /**
     * Wraps source request with a flow. Doesn't check for ongoings.
     *
     * @see [requestShared]
     */
    public fun request(
        key: K,
        dispatcher: CoroutineDispatcher = StandardDispatchers.io,
    ): Flow<V> =
        flow { emit(source(key)) }
            .flowOn(dispatcher)

    /**
     * Makes a request or attaches to an ongoing request
     * if there is any in progress with the given key.
     *
     * @param key request parameters, also used as key for sharing.
     * Must be 1) a data-class or 2) primitive or 3) has equals/hash code implemented for proper distinction.
     * Use [Unit] ot [Int] or look at [CachedSourceNoParams] if there are no parameters for request.
     *
     * @param dispatcher [CoroutineDispatcher] that will be used for request.
     *
     * @return shared flow that other callers with same [key] can be attached to.
     */
    public fun requestShared(
        key: K,
        dispatcher: CoroutineDispatcher = StandardDispatchers.io,
    ): Flow<V> {
        val lazyFlow = suspend {
            ongoingsLock.withLock {

                var ongoingFlow = ongoings[key]
                log.debug { "requestShared: $key / ongoing: $ongoingFlow" }

                if (ongoingFlow == null) {
                    val scope = CoroutineScope(dispatcher)
                    // Keep a separate flag to avoid double removing from [ongoings]
                    // which can be a mistake if another stream adds to [ongoings] after the first removal
                    val isInOngoings = AtomicBoolean(true)
                    ongoingFlow =
                        flow { emit(source(key)) }
                            .flowOn(dispatcher)
                            .map { Result.success(it) }
                            .catch { emit(Result.failure(it)) }
                            .take(1)
                            .onEach {
                                // It's better to release ongoing earlier than .onCompletion()
                                // but only .onCompletion() will be called on cancellation
                                // so try in both places
                                removeOngoing(key, isInOngoings)
                            }
                            .onCompletion {
                                log.debug { "requestShared onCompletion: $key" }
                                removeOngoing(key, isInOngoings)
                                scope.cancel()
                            }
                            .shareIn(
                                scope,
                                SharingStarted.WhileSubscribed(),
                                1,
                            )
                            .take(1)
                            // Shared flow doesn't throw exceptions so wrap and re-throw possible exceptions
                            .map {
                                if (it.isSuccess) {
                                    it.getOrThrow()
                                }
                                else {
                                    throw it.exceptionOrNull()!!
                                }
                            }
                    ongoings[key] = ongoingFlow
                }
                ongoingFlow
            }
        }
        return flow {
            emitAll(lazyFlow())
        }
    }

    internal suspend fun getOngoingSize() =
        ongoingsLock.withLock {
            val size = ongoings.size
            log.debug { "getOngoingSize: $size" }
            size
        }

    private suspend fun removeOngoing(key: K, isInOngoings: AtomicBoolean) =
        withContext(NonCancellable) {
            try {
                ongoingsLock.withLock {
                    if (isInOngoings.value) {
                        ongoings.remove(key)
                        isInOngoings.value = false
                        log.debug { "requestShared removeOngoing: $key, size: ${ongoings.size}" }
                    }
                }
            }
            catch (t: Throwable) {
                log.debug { "requestShared removeOngoing -> error in lock: $t" }
                throw t
            }
        }

    public companion object {

        private val log = logging()
    }
}
