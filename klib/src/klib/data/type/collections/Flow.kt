package klib.data.type.collections

import io.ktor.util.cio.ChannelWriteException
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.close
import io.ktor.utils.io.writeByteArray
import io.ktor.utils.io.writeStringUtf8
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

public fun MutableSharedFlow<*>.onHasSubscriptionChange(
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    change: (Boolean) -> Unit,
): Job = subscriptionCount
    .map { count -> count > 0 }
    .distinctUntilChanged()
    .onEach(change)
    .launchIn(coroutineScope)

public suspend inline fun Flow<ByteArray>.writeToChannel(channel: ByteWriteChannel): Unit = try {
    collect { value ->
        channel.writeByteArray(value)
        channel.flush()
    }
}
catch (e: ChannelWriteException) {
    channel.cancel(e)
}
finally {
    channel.flushAndClose()
}
