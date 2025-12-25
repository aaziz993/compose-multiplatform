package klib.data.type.collections.bimap

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MapTest {

    private fun subject(): Map<Int, String> = mapOf(1 to "1", 2 to "2", 3 to "3")

    @Test
    fun sizeShouldBe3() {
        assertEquals(3, subject().size)
    }

    @Test
    fun shouldNotBeEmpty() {
        assertFalse(subject().isEmpty())
    }

    @Test
    fun shouldContainAllSpecifiedKeys() {
        assertEquals(setOf(1, 2, 3), subject().keys)
    }

    @Test
    fun shouldContainAllSpecifiedValues() {
        assertEquals(listOf("1", "2", "3"), subject().values.toList())
    }

    @Test
    fun shouldContainAllSpecifiedEntries() {
        val expectedEntries = setOf(1 to "1", 2 to "2", 3 to "3")
        val actualEntries = subject().entries.map { it.key to it.value }.toSet()
        assertEquals(expectedEntries, actualEntries)
    }

    @Test
    fun getWithExistedKeyShouldReturnValue() {
        val key = 1
        val value = "1"
        assertTrue(subject().containsKey(key))
        assertEquals(value, subject()[key])
    }

    @Test
    fun getWithNonExistedKeyShouldReturnNull() {
        val key = 4
        assertFalse(subject().containsKey(key))
        assertNull(subject()[key])
    }
}
