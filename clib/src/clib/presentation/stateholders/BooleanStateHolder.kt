package clib.presentation.stateholders

import kotlinx.coroutines.flow.update

public class BooleanStateHolder(value: Boolean) : PrimitiveStateHolder<Boolean>(value) {

    public fun toggle(): Unit = _state.update { !it }
}
