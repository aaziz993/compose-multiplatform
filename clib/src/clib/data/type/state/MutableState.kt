package clib.data.type.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.structuralEqualityPolicy
import klib.data.type.primitives.time.nowEpochMillis

public fun <T> mutableStateOf(
    debounceTime: Long,
    initialValue: T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy(),
): MutableState<T> {
    var lastUpdate = nowEpochMillis
    return mutableStateOf(initialValue, policy).bimap(
        read = { it },
        write = { newValue ->
            newValue.takeIf { nowEpochMillis - lastUpdate > debounceTime }
                ?.also { lastUpdate = nowEpochMillis } ?: value
        },
    )
}

public fun <T, R> MutableState<T>.bimap(
    read: (T) -> R,
    write: State<T>.(R) -> T,
): MutableState<R> =
    object : MutableState<R> {
        override var value: R
            get() = read(this@bimap.value)
            set(value) {
                this@bimap.value = write(value)
            }

        override fun component1(): R = value
        override fun component2(): (R) -> Unit = { this@bimap.value = write(it) }
    }

