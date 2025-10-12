package clib.data.type.collections.restartableflow

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow

public class RestartableMutableStateFlow<T>(
    private val stateFlow: RestartableStateFlow<T>,
    private val mutableStateFlow: MutableStateFlow<T>,
) : RestartableStateFlow<T> by stateFlow, MutableStateFlow<T> by mutableStateFlow {

    override var value: T
        get() = stateFlow.value
        set(value) {
            mutableStateFlow.value = value
        }

    override val replayCache: List<T>
        get() = stateFlow.replayCache

    override suspend fun collect(collector: FlowCollector<T>): Nothing =
        stateFlow.collect(collector)
}
