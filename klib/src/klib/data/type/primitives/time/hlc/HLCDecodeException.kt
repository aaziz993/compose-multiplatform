package klib.data.type.primitives.time.hlc

// Base exception wrapping decode errors
public sealed class HLCDecodeException(message: String? = null) : Exception(message) {

    public data class TimestampDecodeException(val encodedClock: String) :
        HLCDecodeException("Failed to decode timestamp from HLC: $encodedClock")

    public data class CounterDecodeException(val encodedClock: String) :
        HLCDecodeException("Failed to decode counter from HLC: $encodedClock")

    public data class NodeDecodeException(val encodedClock: String) :
        HLCDecodeException("Failed to decode node from HLC: $encodedClock")
}
