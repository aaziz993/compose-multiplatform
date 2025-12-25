package klib.data.type.collections.bimap

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class MutableBiMapTest {

    private fun subject(): MutableBiMap<Int, String> = mutableBiMapOf(1 to "1", 2 to "2", 3 to "3")

    @Test
    fun inverseTwiceShouldBeSame() {
        val map = subject()
        assertEquals(map, map.inverse.inverse)
    }

    @Test
    fun putNewKeyAndValue() {
        val map = subject()
        val previous = map.put(4, "4")
        assertTrue(map.containsKey(4))
        assertTrue(map.containsValue("4"))
        assertEquals("4", map[4])
        assertEquals(null, previous)
        assertEquals(4, map.size)
    }

    @Test
    fun putExistingKeyNewValueRemovesOldValue() {
        val map = subject()
        map[3] = "4"
        assertTrue(map.containsKey(3))
        assertTrue(map.containsValue("4"))
        assertFalse(map.containsValue("3"))
        assertEquals("4", map[3])
    }

    @Test
    fun putNewKeyExistingValueThrows() {
        val map = subject()
        assertFailsWith<IllegalArgumentException> { map[4] = "3" }
    }

    @Test
    fun forcePutNewKeyExistingValueRemovesPreviousKey() {
        val map = subject()
        map.put(4, "3")
        assertTrue(map.containsKey(4))
        assertTrue(map.containsValue("3"))
        assertEquals("3", map[4])
        assertFalse(map.containsKey(3))
    }

    @Test
    fun forcePutExistingKeyExistingValueUnchanged() {
        val map = subject()
        val previous = map.put(3, "3")
        assertEquals(previous, map[3])
    }

    @Test
    fun putAllAddsMultipleEntries() {
        val map = subject()
        map.putAll(mapOf(4 to "4", 5 to "5", 6 to "6"))
        assertEquals("4", map[4])
        assertEquals("5", map[5])
        assertEquals("6", map[6])
    }

    @Test
    fun removeExistingKey() {
        val map = subject()
        map.remove(1)
        assertFalse(map.containsKey(1))
    }

    @Test
    fun removeUnboundKey() {
        val map = subject()
        assertFalse(map.containsKey(4))
        map.remove(4)
        assertFalse(map.containsKey(4))
    }

    @Test
    fun clearShouldEmptyMap() {
        val map = subject()
        map.clear()
        assertTrue(map.isEmpty())
    }

    // Additional factory conversions
    @Test
    fun mapToMutableBiMap() {
        val map = mapOf(1 to "1", 2 to "2", 3 to "3").toMutableBiMap()
        assertEquals(subject(), map)
    }

    @Test
    fun inverseMutableBiMap() {
        val map = mutableBiMapOf("1" to 1, "2" to 2, "3" to 3).inverse
        val expected = mutableBiMapOf(1 to "1", 2 to "2", 3 to "3")
        assertEquals(expected, map)
    }
}
