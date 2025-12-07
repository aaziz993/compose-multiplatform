package klib.data.auth.otp

import kotlin.time.Instant
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DateTimeUnit.Companion.SECOND
import kotlinx.datetime.plus
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
public enum class TotpPeriod(
    step: Int,
    unit: DateTimeUnit.TimeBased,
) {

    Fifteen(15, SECOND), Thirty(30, SECOND), Sixty(60, SECOND);

    @Transient
    public val millis: Long = Instant
        .fromEpochMilliseconds(0L)
        .plus(step, unit)
        .toEpochMilliseconds()
}



