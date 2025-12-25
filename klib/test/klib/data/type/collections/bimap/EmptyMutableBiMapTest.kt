package klib.data.type.collections.bimap

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EmptyMutableBiMapTest {

    private fun subject(): MutableBiMap<Int, String> = mutableBiMapOf()

    @Test
    fun equalsAnotherEmptyMutableBiMap() {
        assertEquals(
            mutableBiMapOf(),
            subject(),
        )
    }

    @Test
    fun hasSameHashCodeAsAnotherEmptyMutableBiMap() {
        assertEquals(
            mutableBiMapOf<Int, String>().hashCode(),
            subject().hashCode(),
        )
    }

    @Test
    fun containsNoValues() {
        assertEquals(
            emptySet(),
            subject().values,
        )
    }

    @Test
    fun inverseIsStillEmpty() {
        assertEquals(
            emptyBiMap(),
            subject().inverse,
        )
    }

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
            emptySet(),
            subject().keys,
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
