package clib.presentation.stateholders

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

public open class PrimitiveStateHolder<T>(value: T) {

    protected val _state: MutableStateFlow<T> = MutableStateFlow(value)

    public val state: StateFlow<T> = _state.asStateFlow()

    public fun setValue(value: T): Unit = _state.update { value }
}
