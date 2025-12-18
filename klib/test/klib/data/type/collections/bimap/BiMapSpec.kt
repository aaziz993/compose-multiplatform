package klib.data.type.collections.bimap

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.kotest.core.spec.style.FunSpec
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.subject.SubjectSpek
import org.jetbrains.spek.subject.itBehavesLike
import kotlin.test.assertTrue

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

public object BiMapSpec : FunSpec(
    {

        val subject = biMapOf(
            1 to "1",
            2 to "2",
            3 to "3",
        )

        include(MapSpec(subject))

        context("""a bimap contains { 1 to "1", 2 to "2", 3 to "3" }""") {

            test("should be equal to another bimap with same content") {
                subject shouldBe biMapOf(
                    1 to "1",
                    2 to "2",
                    3 to "3",
                )
            }

            test("should not be equal to another bimap with different content") {
                subject shouldNotBe biMapOf(1 to "1", 2 to "2")
                subject shouldNotBe biMapOf(1 to "1", 2 to "2", 3 to "4")
                subject shouldNotBe biMapOf(1 to "1", 2 to "2", 3 to "3", 4 to "4")
            }

            test("should have same hash code with another bimap with same content") {
                subject.hashCode() shouldBe
                    biMapOf(1 to "1", 2 to "2", 3 to "3").hashCode()
            }

            test("should contain all specified values") {
                subject.values shouldContainExactly setOf("1", "2", "3")
            }

            context("inverse") {
                test("should map from specified values to specified keys") {
                    subject.inverse shouldBe biMapOf(
                        "1" to 1,
                        "2" to 2,
                        "3" to 3,
                    )
                }
            }
        }

        context("a bimap with one element") {
            val biMap = biMapOf(1 to "1")

            test("should contain specified key and value") {
                biMap shouldBe biMapOf(1 to "1", 1 to "1")
            }
        }
    },
)

public object ToBiMapSpec : FunSpec(
    {

        val subject = mapOf(
            1 to "1",
            2 to "2",
            3 to "3",
        ).toBiMap()

        include(BiMapSpec())
    },
)
