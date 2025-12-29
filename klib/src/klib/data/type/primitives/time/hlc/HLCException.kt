package klib.data.type.primitives.time.hlc

public sealed class HLCException(message: String? = null) : Exception(message) {

    public data class DuplicateNodeException(val nodeID: NodeID) :
        HLCException("Duplicate node detected: $nodeID")

    public data class ClockDriftException(val local: Timestamp, val now: Timestamp) :
        HLCException("Clock drift too large: local=$local, now=$now")

    public object CausalityOverflowException :
        HLCException("HLC counter exceeded maximum value")
}
