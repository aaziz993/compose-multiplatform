package klib.data.type.primitives.time

import klib.data.coroutines.StandardDispatchers
import kotlin.time.Duration
import kotlinx.coroutines.CoroutineScope

public expect class CountDownTimer(
    initial: Duration,
    interval: Duration,
    onTick: (Duration) -> Unit,
    onFinish: () -> Unit = {},
    scope: CoroutineScope = CoroutineScope(StandardDispatchers.main),
) {

    public fun start()
    public fun cancel()
}
