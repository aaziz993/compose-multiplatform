@file:OptIn(BetaInteropApi::class)

package klib.data.type.primitives.time

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSDate
import platform.Foundation.NSTimer
import platform.Foundation.timeIntervalSince1970

public actual class CountDownTimer actual constructor(
    private val initial: Duration,
    private val interval: Duration,
    private val onTick: (Duration) -> Unit,
    private val onFinish: () -> Unit,
) {

    private var iosTimer: NSTimer? = null
    private var targetTimeMillis: Long = 0L

    @ObjCAction
    public fun onTimerTick(timer: NSTimer) {
        // Get the current time in milliseconds
        val currentTimeMillis = (NSDate().timeIntervalSince1970 * 1000).toLong()

        // Calculate the remaining time
        val millisUntilFinished = targetTimeMillis - currentTimeMillis

        if (millisUntilFinished <= 0) {
            // If the countdown is complete, call the finish method
            onTimerFinish(timer)
            return
        }

        // Trigger the onTick callback with the correct time values
        onTick(millisUntilFinished.toDuration(DurationUnit.MILLISECONDS))
    }

    @ObjCAction
    public fun onTimerFinish(timer: NSTimer) {
        onFinish()
        cancel() // Stop the timer
    }

    public actual fun start() {
        // Calculate the target time in milliseconds (future time)
        val timeInFuture = initial.inWholeMilliseconds
        targetTimeMillis = (NSDate().timeIntervalSince1970 * 1000).toLong() + timeInFuture

        // Schedule the timer with the given interval
        iosTimer = NSTimer.scheduledTimerWithTimeInterval(
            interval = interval.inWholeMilliseconds / 1000.0,
            repeats = true,
        ) { _ ->
            onTimerTick(iosTimer!!)
        }
    }

    public actual fun cancel() {
        iosTimer?.invalidate()
    }
}
