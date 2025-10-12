package klib.data.type.primitives.time

import kotlin.time.Duration

public expect class CountDownTimer(
    initial: Duration,
    interval: Duration,
    onTick: (Duration) -> Unit,
    onFinish: () -> Unit = {},
) {

    public fun start()
    public fun cancel()
}
