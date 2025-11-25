package clib.data.type.state

import androidx.compose.runtime.State

public fun <T, R> State<T>.map(block: (T) -> R): State<R> =
    object : State<R> {
        override val value: R get() = block(this@map.value)
    }
