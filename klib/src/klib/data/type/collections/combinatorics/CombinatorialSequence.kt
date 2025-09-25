package klib.data.type.collections.combinatorics

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger

/**
 * This [CombinatorialSequence] class has the [totalSize] property of the total number of sequences.
 * This sequence can be iterated only once.
 *
 * @property totalSize Total number of elements of this sequence.
 *
 * @constructor Construct from sequence.
 *
 * @see Sequence
 * @see constrainOnce
 */
public class CombinatorialSequence<out T>(
    public val totalSize: BigInteger,
    sequence: Sequence<T>
) : Sequence<T> by sequence.constrainOnce() {

    /**
     * Construct from iterator.
     */
    public constructor(totalSize: BigInteger, iterator: Iterator<T>) : this(totalSize, iterator.asSequence())

    /**
     * Returns a [List] containing all elements.
     *
     * The operation is _terminal_.
     */
    public fun toList(): List<T> = toMutableList()

    /**
     * Returns a [MutableList] containing all elements.
     *
     * The operation is _terminal_.
     */
    public fun toMutableList(): MutableList<out T> = if (totalSize <= MAX_ARRAY_SIZE) {
        toCollection(ArrayList(totalSize.intValue(true)))
    } else {
        toCollection(mutableListOf())
    }

    public companion object {

        /**
         * The value is 2147483639.
         *
         */
        private val MAX_ARRAY_SIZE = (Int.MAX_VALUE - 8).toBigInteger()
    }
}
