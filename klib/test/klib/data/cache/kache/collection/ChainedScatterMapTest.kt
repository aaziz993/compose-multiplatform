package klib.data.cache.kache.collection

import klib.data.cache.kache.collection.MutableChain
import klib.data.cache.kache.collection.MutableChainedScatterMap
import kotlin.test.Test
import kotlin.test.assertEquals

class ChainedScatterMapTest {

    @Test
    fun retainElementsAfterInternalResize() {
        val map = MutableChainedScatterMap<String, Int>(
            // The least possible capacity is 7
            initialCapacity = 7,
            accessChain = MutableChain(),
        )

        repeat(8) {
            map["$it"] = it
        }

        assertEquals(8, map.size)
        repeat(8) {
            assertEquals(it, map["$it"])
        }
    }
}
