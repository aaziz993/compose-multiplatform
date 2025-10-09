package klib.data.type.primitives.time

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.CoroutineScope

public actual class CountDownTimer actual constructor(
    private val initial: Duration,
    private val interval: Duration,
    private val onTick: (Duration) -> Unit,
    private val onFinish: () -> Unit,
    private val scope: CoroutineScope,
) {

    private var androidCountDownTimer: android.os.CountDownTimer? = null

    public actual fun start() {
        androidCountDownTimer = object : android.os.CountDownTimer(
            initial.inWholeMilliseconds,
            interval.inWholeMilliseconds,
        ) {
            override fun onTick(millisUntilFinished: Long) {
                onTick(millisUntilFinished.toDuration(DurationUnit.MILLISECONDS))
            }

            override fun onFinish() {
                this@CountDownTimer.onFinish()
            }
        }.start()
    }

    public actual fun cancel() {
        androidCountDownTimer?.cancel()
    }
}
