package clib.data.type.collections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
public fun <T, R> StateFlow<T>.map(
    scope: CoroutineScope,
    transform: (data: T) -> R
): StateFlow<R> = mapLatest(transform).stateIn(scope, SharingStarted.Eagerly, transform(value))

@OptIn(ExperimentalCoroutinesApi::class)
public fun <T, R> StateFlow<T>.map(
    scope: CoroutineScope,
    initialValue: R,
    transform: suspend (data: T) -> R
): StateFlow<R> = mapLatest(transform).stateIn(scope, SharingStarted.Eagerly, initialValue)

@Composable
public fun <T> Flow<T>.toLaunchedEffect(
    vararg keys: Any?,
    onEvent: (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(this, lifecycleOwner.lifecycle, *keys) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                collect(onEvent)
            }
        }
    }
}
