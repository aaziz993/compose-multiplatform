package klib.data.type.collections.bimap

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BiMapTest {

    private fun subject(): BiMap<Int, String> =
        biMapOf(1 to "1", 2 to "2", 3 to "3")

    @Test
    fun equalToAnotherBimapWithSameContent() {
        assertEquals(
            subject(),
            biMapOf(1 to "1", 2 to "2", 3 to "3"),
        )
    }

    @Test
    fun notEqualToBimapWithDifferentContent() {
        val subject = subject()

        assertNotEquals(subject, biMapOf(1 to "1", 2 to "2"))
        assertNotEquals(subject, biMapOf(1 to "1", 2 to "2", 3 to "4"))
        assertNotEquals(subject, biMapOf(1 to "1", 2 to "2", 3 to "3", 4 to "4"))
    }

    @Test
    fun sameHashCodeForSameContent() {
        assertEquals(
            subject().hashCode(),
            biMapOf(1 to "1", 2 to "2", 3 to "3").hashCode(),
        )
    }

    @Test
    fun containsAllSpecifiedValues() {
        assertEquals(
            setOf("1", "2", "3"),
            subject().values,
        )
    }

    @Test
    fun inverseMapsValuesToKeys() {
        assertEquals(
            biMapOf("1" to 1, "2" to 2, "3" to 3),
            subject().inverse,
        )
    }

    @Test
    fun singleElementBimapContainsSpecifiedKeyAndValue() {
        val biMap = biMapOf(1 to "1")

        assertEquals(
            biMapOf(1 to "1"),
            biMap,
        )
    }
}

class ToBiMapTest {

    private fun subject(): BiMap<Int, String> =
        mapOf(1 to "1", 2 to "2", 3 to "3").toBiMap()

    @Test
    fun behavesLikeBiMap() {
        assertEquals(
            biMapOf(1 to "1", 2 to "2", 3 to "3"),
            subject(),
        )

        assertEquals(
            biMapOf("1" to 1, "2" to 2, "3" to 3),
            subject().inverse,
        )
    }
}
