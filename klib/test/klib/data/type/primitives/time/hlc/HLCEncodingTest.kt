package klib.data.type.primitives.time.hlc

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.uuid.Uuid

class HLCEncodingTest {

    @Test
    fun `test that a hlc encodes to a string correctly`() {
        val epochMillis = 943920000000L
        val counter = 15
        val node = Uuid.random()

        val clock = HybridLogicalClock(Timestamp(epochMillis), NodeID.mint(node), counter)

        val expected = "${epochMillis.toString().padStart(15, '0')}:${counter.toString(36).padStart(5, '0')}:${node.toString().replace("-", "").takeLast(16)}"

        assertEquals(expected, HybridLogicalClock.encodeToString(clock))
    }

    @Test
    fun `test that a hlc decodes from a string correctly`() {
        val epochMillis = 943920000000L
        val counter = 15
        val node = Uuid.random().toString().replace("-", "").takeLast(16)

        val encoded = "${epochMillis.toString().padStart(15, '0')}:${counter.toString(36).padStart(5, '0')}:$node"
        val decoded = HybridLogicalClock.decodeFromString(encoded)

        assertTrue(decoded.fold({ true }, { false }))
        assertEquals(decoded.getOrNull()?.timestamp?.epochMillis, epochMillis)
        assertEquals(decoded.getOrNull()?.counter, counter)
        assertEquals(decoded.getOrNull()?.node?.identifier, node)
    }
}
