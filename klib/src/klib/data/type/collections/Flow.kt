package klib.data.type.collections

import io.ktor.util.cio.use
import io.ktor.utils.io.ByteWriteChannel
import klib.data.net.http.client.writeByteArrayWithLength
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

public fun MutableSharedFlow<*>.onHasSubscriptionChange(
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    change: (Boolean) -> Unit,
): Job = subscriptionCount
    .map { count -> count > 0 }
    .distinctUntilChanged()
    .onEach(change)
    .launchIn(coroutineScope)

public suspend inline fun Flow<ByteArray>.writeToChannel(channel: ByteWriteChannel): Unit = channel.use {
    collect { value ->
        channel.writeByteArrayWithLength(value)
        channel.flush()
    }
}
