package klib.data.type.collections.combinatorics

import com.ionspin.kotlin.bignum.integer.BigInteger
import klib.data.type.collections.combinatorics.internal.combinationsWithRepetition
import klib.data.type.collections.combinatorics.internal.mapToArray
import kotlin.jvm.JvmStatic

/**
 * The class [CombinationsWithRepetitionGenerator] contains methods for generating combinations with repetition.
 */
public object CombinationsWithRepetitionGenerator {

    @PublishedApi
    internal inline fun <R> build(n: Int, r: Int,
                                  crossinline transform: (IntArray) -> R): CombinatorialSequence<R> {
        val totalSize = combinationsWithRepetition(n, r)

        @Suppress("ObjectPropertyName")
        val iterator = object : Iterator<R> {
            val indices = IntArray(r)
            var _hasNext = true

            override fun hasNext(): Boolean = _hasNext

            override fun next(): R {
                if (!hasNext()) throw NoSuchElementException()
                val nextValue = transform(indices)
                for (i in r - 1 downTo 0) {
                    if (indices[i] != n - 1) {
                        val v = indices[i] + 1
                        for (j in i until r) {
                            indices[j] = v
                        }
                        return nextValue
                    }
                }
                _hasNext = false
                return nextValue
            }
        }

        return CombinatorialSequence(totalSize, iterator)
    }

    /**
     * Returns a sequence of [r] number of combinations with repetition of [n] elements.
     *
     * @throws IllegalArgumentException if [r] is negative.
     */
    @JvmStatic
    public fun indices(n: Int, r: Int): CombinatorialSequence<IntArray> {
        require(r >= 0) { "r must be non-negative, was $r" }

        if (r == 0) {
            return CombinatorialSequence(BigInteger.ONE, sequenceOf(intArrayOf()))
        } else if (n < 1) {
            return CombinatorialSequence(BigInteger.ZERO, emptySequence())
        }

        return build(n, r) { it.copyOf() }
    }

    /**
     * Returns a sequence of combinations with repetition of [length] of the elements of [iterable].
     *
     * @throws IllegalArgumentException if [length] is negative.
     */
    @JvmStatic
    public fun <T> generate(iterable: Iterable<T>, length: Int): CombinatorialSequence<List<T>> {
        require(length >= 0) { "length must be non-negative, was $length" }

        if (length == 0) {
            return CombinatorialSequence(BigInteger.ONE, sequenceOf(emptyList()))
        }

        val pool = iterable.toList()
        val n = pool.size

        if (n < 1) {
            return CombinatorialSequence(BigInteger.ZERO, emptySequence())
        }

        return build(n, length) { ints -> ints.map { pool[it] } }
    }

    /**
     * Returns a sequence of combinations with repetition of [length] of the elements of [array].
     *
     * @throws IllegalArgumentException if [length] is negative.
     */
    public inline fun <reified T> generate(array: Array<T>, length: Int): CombinatorialSequence<Array<T>> {
        require(length >= 0) { "length must be non-negative, was $length" }

        val n = array.size

        if (length == 0) {
            return CombinatorialSequence(BigInteger.ONE, sequenceOf(emptyArray()))
        } else if (n < 1) {
            return CombinatorialSequence(BigInteger.ZERO, emptySequence())
        }

        val pool = array.copyOf()

        return build(n, length) { ints -> ints.mapToArray { pool[it] } }
    }
}
