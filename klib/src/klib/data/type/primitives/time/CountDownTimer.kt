package klib.data.type.primitives.time

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public expect class CountDownTimer(
    initial: Duration,
    interval: Duration = 1.seconds,
    onTick: (Duration) -> Unit,
    onFinish: () -> Unit = {},
) {

    public fun start()
    public fun cancel()
}
