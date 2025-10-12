// jvmMain
package klib.data.type.primitives.time

import kotlin.time.Duration
import kotlinx.coroutines.*

public actual class CountDownTimer actual constructor(
    private val initial: Duration,
    private val interval: Duration,
    private val onTick: (Duration) -> Unit,
    private val onFinish: () -> Unit,
) {

    private var job: Job? = null

    public actual fun start() {
        job?.cancel()
        job = CoroutineScope(Dispatchers.Default).launch {
            var remaining = initial
            while (remaining > Duration.ZERO) {
                onTick(remaining)
                delay(interval)
                remaining -= interval
            }
            onFinish()
        }
    }

    public actual fun cancel() {
        job?.cancel()
    }
}
