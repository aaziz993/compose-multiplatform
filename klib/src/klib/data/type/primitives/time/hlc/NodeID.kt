package klib.data.type.primitives.time.hlc

import kotlin.jvm.JvmInline
import kotlin.uuid.Uuid

@JvmInline
public value class NodeID(public val identifier: String) {

    public companion object {

        public fun mint(uuid: Uuid = Uuid.random()): NodeID =
            NodeID(uuid.toString().replace("-", "").takeLast(16))
    }
}
