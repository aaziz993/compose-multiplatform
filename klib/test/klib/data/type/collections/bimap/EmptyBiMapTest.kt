package klib.data.type.collections.bimap

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EmptyBiMapTest {

    private fun subject(): BiMap<Int, String> = emptyBiMap()

    @Test
    fun equalToAnotherEmptyBimap() {
        assertTrue(subject() == emptyBiMap<Int, String>())
    }

    @Test
    fun sameHashCodeAsAnotherEmptyBimap() {
        assertEquals(
            emptyBiMap<Int, String>().hashCode(),
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
    fun inverseIsStillEmptyBimap() {
        assertEquals(
            emptyBiMap(),
            subject().inverse,
        )
    }
}
