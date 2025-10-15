package klib.data.type.primitives.time.model

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
public data class TimeRange(
    override val endInclusive: LocalTime,
    override val start: LocalTime
) : ClosedRange<LocalTime>
