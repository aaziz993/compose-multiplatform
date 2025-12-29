package klib.data.type.primitives.time.hlc

import kotlin.time.Clock
import kotlin.jvm.JvmInline

@JvmInline
public value class Timestamp(public val epochMillis: Long) {

    public companion object {

        public fun now(clock: Clock): Timestamp = Timestamp(clock.now().toEpochMilliseconds())
    }
}
