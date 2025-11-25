package clib.data.type.collections

import androidx.compose.runtime.MutableState
import com.diamondedge.logging.KmLog

public fun <T> MutableState<T>.withLogging(log: KmLog): MutableState<T> {
    return object : MutableState<T> by this {
        override var value: T
            get() = this@withLogging.value
            set(value) {
                log.debug { "State changed: ${this@withLogging.value} -> $value" }
                this@withLogging.value = value
            }
    }
}
