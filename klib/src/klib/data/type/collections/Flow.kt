package klib.data.type.collections

import io.ktor.util.cio.use
import io.ktor.utils.io.ByteWriteChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

// The reasoning 5_000 was chosen for the stopTimeoutMillis can be found in the official Android documentation, which discusses the ANR (Application Not Responding) timeout threshold.
public const val SHARING_STARTED_STOP_TIMEOUT_MILLIS: Long = 5_000

public suspend inline fun Flow<ByteArray>.writeToChannel(channel: ByteWriteChannel): Unit = channel.use {
    collect { value ->
        channel.writeByteArrayWithLength(value)
        channel.flush()
    }
}

public fun <T, R> StateFlow<T>.map(
    scope: CoroutineScope,
    initialValue: R,
    transform: suspend (value: T) -> R
): StateFlow<R> = mapLatest(transform).stateIn(scope, SharingStarted.Eagerly, initialValue)

public fun MutableSharedFlow<*>.onHasSubscriptionChange(
    change: (Boolean) -> Unit,
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
): Job = subscriptionCount
    .map { count -> count > 0 }
    .distinctUntilChanged()
    .drop(1) // Drop first false event
    .onEach(change)
    .launchIn(coroutineScope)
