// jsMain
package klib.data.type.primitives.time

import js.date.Date
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration
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
        val targetTime = Date.now() + initial.inWholeMilliseconds
        job = CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                val remaining = (targetTime - Date.now()).toLong()
                if (remaining <= 0) break

                onTick(remaining.toDuration(DurationUnit.MILLISECONDS))

                delay(interval)
            }
            onFinish()
        }
    }

    public actual fun cancel() {
        job?.cancel()
    }
}
