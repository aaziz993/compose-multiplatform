package klib.data.type.collections.bimap

import io.kotest.core.spec.KotestTestScope
import io.kotest.core.spec.style.FunSpec
import klib.data.type.collections.bimap.biMapOf
import kotlin.test.assertNotEquals
import kotlin.test.assertEquals

@KotestTestScope
object BiMapCoverageSpec : FunSpec(
    {
        test("tests for coverage") {
            assertNotEquals(biMapOf(), mapOf<Int, String>())
            assertNotEquals(biMapOf<Int, String?>(1 to null), biMapOf<Int, String?>(1 to "1"))
            assertNotEquals(biMapOf<Int, String?>(1 to null), biMapOf())
            assertEquals(biMapOf<Int, String?>(1 to null), biMapOf<Int, String?>(1 to null))
            assertNotEquals(biMapOf<Int?, String?>(null to null), biMapOf())
        }
    },
)
