package klib.data.type.collections.bimap

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EmptyMapTest {

    private fun subject(): Map<Int, String> = emptyMap()

    @Test
    fun sizeIsZero() {
        assertEquals(0, subject().size)
    }

    @Test
    fun isEmpty() {
        assertTrue(subject().isEmpty())
    }

    @Test
    fun containsNoKeys() {
        assertEquals(
            emptySet<Int>(),
            subject().keys,
        )
    }

    @Test
    fun containsNoValues() {
        assertEquals(
            emptyList<String>(),
            subject().values.toList(),
        )
    }

    @Test
    fun containsNoEntries() {
        assertEquals(
            emptySet<Map.Entry<Int, String>>(),
            subject().entries,
        )
    }
}
