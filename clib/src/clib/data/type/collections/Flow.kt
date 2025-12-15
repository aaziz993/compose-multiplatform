package clib.data.type.collections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect as ComposeLaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Suppress("ComposeUnstableReceiver")
@Composable
public fun <T> Flow<T>.LaunchedEffect(
    vararg keys: Any?,
    onEvent: (T) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    ComposeLaunchedEffect(this, lifecycleOwner.lifecycle, *keys) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                collect(onEvent)
            }
        }
    }
}
