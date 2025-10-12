package clib.data.type.collections.restartableflow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted

public interface RestartableMutableStateFlow<T> : RestartableStateFlow<T>, MutableStateFlow<T>

public fun <T> Flow<T>.restartableStateIn(
    mutableStateFlow: MutableStateFlow<T>,
    started: SharingStarted,
    scope: CoroutineScope,
    initialValue: T,
): RestartableMutableStateFlow<T> {
    val restartableStateFlow = restartableStateIn(started, scope, initialValue)

    return object : RestartableMutableStateFlow<T>, MutableStateFlow<T> by mutableStateFlow,
        RestartableStateFlow<T> by restartableStateFlow {
        override var value: T
            get() = mutableStateFlow.value
            set(value) {
                mutableStateFlow.value = value
            }

        override val replayCache: List<T>
            get() = restartableStateFlow.replayCache

        override suspend fun collect(collector: FlowCollector<T>): Nothing =
            restartableStateFlow.collect(collector)
    }
}
