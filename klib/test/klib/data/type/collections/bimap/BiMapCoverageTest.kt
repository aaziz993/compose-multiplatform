package klib.data.type.collections.bimap

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BiMapCoverageTest {

    @Test
    fun testsForCoverage() {
        assertNotEquals(biMapOf(), mapOf<Int, String>())

        assertNotEquals(
            biMapOf<Int, String?>(1 to null),
            biMapOf<Int, String?>(1 to "1"),
        )

        assertNotEquals(
            biMapOf<Int, String?>(1 to null),
            biMapOf(),
        )

        assertEquals(
            biMapOf<Int, String?>(1 to null),
            biMapOf<Int, String?>(1 to null),
        )

        assertNotEquals(
            biMapOf<Int?, String?>(null to null),
            biMapOf(),
        )
    }
}
