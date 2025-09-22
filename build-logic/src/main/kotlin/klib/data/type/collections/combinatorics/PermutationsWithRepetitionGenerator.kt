package klib.data.type.collections.combinatorics

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import klib.data.type.collections.combinatorics.internal.mapToArray

/**
 * The class [PermutationsWithRepetitionGenerator] contains methods for generating permutations with repetition.
 */
public object PermutationsWithRepetitionGenerator {

    @PublishedApi
    internal inline fun <R> build(n: Int, r: Int,
                                  crossinline transform: (IntArray) -> R): CombinatorialSequence<R> {
        val totalSize = n.toBigInteger().pow(r)

        val iterator = if (totalSize <= Long.MAX_VALUE.toBigInteger()) {
            object : Iterator<R> {
                val indices = IntArray(r)
                var t = totalSize.longValue(true)

                override fun hasNext(): Boolean = t > 0

                override fun next(): R {
                    if (!hasNext()){
                        throw NoSuchElementException()
                    }
                    t--
                    val nextValue = transform(indices)
                    for (i in r - 1 downTo 0) {
                        if (indices[i] >= n - 1) {
                            indices[i] = 0
                        } else {
                            indices[i]++
                            break
                        }
                    }
                    return nextValue
                }
            }
        } else {
            object : Iterator<R> {
                val indices = IntArray(r)
                var t = totalSize

                override fun hasNext(): Boolean = t > BigInteger.ZERO

                override fun next(): R {
                    if (!hasNext()) throw NoSuchElementException()
                    t--
                    val nextValue = transform(indices)
                    for (i in r - 1 downTo 0) {
                        if (indices[i] >= n - 1) {
                            indices[i] = 0
                        } else {
                            indices[i]++
                            break
                        }
                    }
                    return nextValue
                }
            }
        }

        return CombinatorialSequence(totalSize, iterator)
    }

    /**
     * Returns a sequence of [r] number of permutations with repetition of [n] elements.
     *
     * @throws IllegalArgumentException if [r] is negative.
     */
    @JvmStatic
    public fun indices(n: Int, r: Int): CombinatorialSequence<IntArray> {
        require(r >= 0) { "r must be non-negative, was $r" }

        if (r == 0) {
            return CombinatorialSequence(BigInteger.ONE, sequenceOf(intArrayOf()))
        }

        return build(n, r) { it.copyOf() }
    }

    /**
     * Returns a sequence of permutations with repetition of [length] of the elements of [iterable].
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

        return build(pool.size, length) { ints -> ints.map { pool[it] } }
    }

    /**
     * Returns a sequence of permutations with repetition of [length] of the elements of [array].
     *
     * @throws IllegalArgumentException if [length] is negative.
     */
    public inline fun <reified T> generate(array: Array<T>, length: Int): CombinatorialSequence<Array<T>> {
        require(length >= 0) { "length must be non-negative, was $length" }

        if (length == 0) {
            return CombinatorialSequence(BigInteger.ONE, sequenceOf(emptyArray()))
        }

        val pool = array.copyOf()

        return build(pool.size, length) { ints -> ints.mapToArray { pool[it] } }
    }
}
