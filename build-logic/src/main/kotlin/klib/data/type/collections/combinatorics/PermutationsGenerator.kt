package klib.data.type.collections.combinatorics

import com.ionspin.kotlin.bignum.integer.BigInteger
import klib.data.type.collections.combinatorics.internal.mapToArray
import klib.data.type.collections.combinatorics.internal.mapToList
import klib.data.type.collections.combinatorics.internal.permutations
import klib.data.type.collections.combinatorics.internal.swap

/**
 * The class [PermutationsGenerator] contains methods for generating permutations.
 */
public object PermutationsGenerator {

    @PublishedApi
    internal inline fun <R> build(n: Int, r: Int,
                                  crossinline transform: (IntArray) -> R): CombinatorialSequence<R> {
        val totalSize = permutations(n, r)

        val iterator = if (n == r) {
            object : Iterator<R> {
                val indices = IntArray(n) { it }
                val lastIndex = n - 1
                var _hasNext = true

                override fun hasNext(): Boolean = _hasNext

                override fun next(): R {
                    if (!hasNext()) throw NoSuchElementException()
                    val nextValue = transform(indices)
                    var i = lastIndex
                    while (i > 0 && indices[i - 1] >= indices[i]) {
                        i--
                    }
                    if (i <= 0) {
                        _hasNext = false
                        return nextValue
                    }
                    var j = lastIndex
                    while (indices[j] <= indices[i - 1]) {
                        j--
                    }
                    indices.swap(i - 1, j)
                    j = lastIndex
                    while (i < j) {
                        indices.swap(i, j)
                        i++
                        j--
                    }
                    return nextValue
                }
            }
        } else {
            object : Iterator<R> {
                val indices = IntArray(n) { it }
                val cycles = IntArray(r) { n - it }
                var _hasNext = true

                override fun hasNext(): Boolean = _hasNext

                override fun next(): R {
                    if (!hasNext()) throw NoSuchElementException()
                    val nextValue = transform(indices)
                    for (i in r - 1 downTo 0) {
                        cycles[i]--
                        if (cycles[i] == 0) {
                            val first = indices[i]
                            for (j in i until n - 1) {
                                indices[j] = indices[j + 1]
                            }
                            indices[n - 1] = first
                            cycles[i] = n - i
                        } else {
                            indices.swap(i, n - cycles[i])
                            return nextValue
                        }
                    }
                    _hasNext = false
                    return nextValue
                }
            }
        }

        return CombinatorialSequence(totalSize, iterator)
    }

    /**
     * Returns a sequence of [r] number of permutations [n] elements.
     *
     * If [r] is not specified or is null, the default for [r] is the length of [n].
     *
     * @throws IllegalArgumentException if [r] is negative.
     */
    @JvmStatic
    public fun indices(n: Int, r: Int? = null): CombinatorialSequence<IntArray> {
        if (r != null) {
            require(r >= 0) { "r must be non-negative, was $r" }
        }

        val r1 = r ?: n

        if (r1 > n) {
            return CombinatorialSequence(BigInteger.ZERO, emptySequence())
        } else if (r1 == 0) {
            return CombinatorialSequence(BigInteger.ONE, sequenceOf(intArrayOf()))
        }

        return build(n, r1) { it.copyOf(r1) }
    }

    /**
     * Returns a sequence of permutations of [length] of the elements of [iterable].
     *
     * If [length] is not specified or is null, the default for [length] is the length of [iterable].
     *
     * @throws IllegalArgumentException if [length] is negative.
     */
    @JvmStatic
    public fun <T> generate(iterable: Iterable<T>, length: Int? = null): CombinatorialSequence<List<T>> {
        if (length != null) {
            require(length >= 0) { "length must be non-negative, was $length" }
        }

        val pool = iterable.toList()
        val n = pool.size
        val r = length ?: n

        if (r > n) {
            return CombinatorialSequence(BigInteger.ZERO, emptySequence())
        } else if (r == 0) {
            return CombinatorialSequence(BigInteger.ONE, sequenceOf(emptyList()))
        }

        return build(n, r) { indices -> indices.mapToList(r) { pool[it] } }
    }

    /**
     * Returns a sequence of permutations of [length] of the elements of [array].
     *
     * If [length] is not specified or is null, the default for [length] is the length of [array].
     *
     * @throws IllegalArgumentException if [length] is negative.
     */
    public inline fun <reified T> generate(array: Array<T>, length: Int? = null): CombinatorialSequence<Array<T>> {
        if (length != null) {
            require(length >= 0) { "length must be non-negative, was $length" }
        }

        val n = array.size
        val r = length ?: n

        if (r > n) {
            return CombinatorialSequence(BigInteger.ZERO, emptySequence())
        } else if (r == 0) {
            return CombinatorialSequence(BigInteger.ONE, sequenceOf(emptyArray()))
        }

        val pool = array.copyOf()

        return build(n, r) { indices -> indices.mapToArray(r) { pool[it] } }
    }
}
