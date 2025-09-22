@file:JvmName("Combinatorics")

package klib.data.type.collections.combinatorics

import com.ionspin.kotlin.bignum.integer.BigInteger

/**
 * The class [Combinatorics] contains methods for generating combinatorial sequence.
 */
public object Combinatorics {

    /**
     * Returns a sequence of permutations of [length] of the elements of [iterable].
     *
     * If [length] is not specified or is null, the default for [length] is the length of [iterable].
     *
     * @throws IllegalArgumentException if [length] is negative.
     */
    @JvmStatic
    @JvmOverloads
    public fun <T> permutations(iterable: Iterable<T>, length: Int? = null): CombinatorialSequence<List<T>> =
        PermutationsGenerator.generate(iterable, length)

    /**
     * Returns a sequence of permutations with repetition of [length] of the elements of [iterable].
     *
     * @throws IllegalArgumentException if [length] is negative.
     */
    @JvmStatic
    public fun <T> permutationsWithRepetition(iterable: Iterable<T>, length: Int): CombinatorialSequence<List<T>> =
        PermutationsWithRepetitionGenerator.generate(iterable, length)

    /**
     * Returns a sequence of derangement of the elements of [iterable].
     */
    @JvmStatic
    public fun <T> derangements(iterable: Iterable<T>): CombinatorialSequence<List<T>> =
        DerangementGenerator.generate(iterable)

    /**
     * Returns a sequence of combinations of [length] of the elements of [iterable].
     *
     * @throws IllegalArgumentException if [length] is negative.
     */
    @JvmStatic
    public fun <T> combinations(iterable: Iterable<T>, length: Int): CombinatorialSequence<List<T>> =
        CombinationsGenerator.generate(iterable, length)

    /**
     * Returns a sequence of combinations with repetition of [length] of the elements of [iterable].
     *
     * @throws IllegalArgumentException if [length] is negative.
     */
    @JvmStatic
    public fun <T> combinationsWithRepetition(iterable: Iterable<T>, length: Int): CombinatorialSequence<List<T>> =
        CombinationsWithRepetitionGenerator.generate(iterable, length)

    /**
     * Returns a sequence of cartesian product of the elements of [iterables].
     *
     * To compute the cartesian product of [iterables] with itself,
     * specify the number of repetitions with the [repeat] named argument.
     *
     * @throws IllegalArgumentException if [repeat] is negative.
     */
//    @SafeVarargs
    @JvmStatic
    @JvmOverloads
    public fun <T> cartesianProduct(vararg iterables: Iterable<T>, repeat: Int = 1): CombinatorialSequence<List<T>> =
        CartesianProductGenerator.generate(*iterables, repeat = repeat)

    /**
     * Returns a sequence of progressive cartesian products of the elements of [iterables].
     *
     * The combinations are generated progressively from **left to right**:
     * - First, the first iterable alone (length = 1)
     * - Then the first two iterables combined (length = 2)
     * - And so on until all iterables are combined
     *
     * Example:
     * ```
     * val list1 = listOf("A", "B")
     * val list2 = listOf("1", "2")
     * val list3 = listOf("x", "y")
     *
     * val result = Combinatorics.progressiveCartesian(list1, list2, list3)
     * result.forEach { println(it) }
     * ```
     * Output:
     * ```
     * [A]
     * [B]
     * [A, 1]
     * [A, 2]
     * [B, 1]
     * [B, 2]
     * [A, 1, x]
     * [A, 1, y]
     * [A, 2, x]
     * [A, 2, y]
     * [B, 1, x]
     * [B, 1, y]
     * [B, 2, x]
     * [B, 2, y]
     * ```
     *
     * @param iterables Vararg of iterables to combine progressively.
     * @return A [CombinatorialSequence] containing all progressive combinations.
     */
    @JvmStatic
    @JvmOverloads
    public fun <T> progressiveCartesianProduct(vararg iterables: Iterable<T>): CombinatorialSequence<List<T>> {
        if (iterables.isEmpty())
            return CombinatorialSequence(BigInteger.ZERO, sequenceOf())

        // Compute total size eagerly using totalSize property
        var total: BigInteger = BigInteger.ZERO
        val partials = iterables.indices.map { index ->
            val suffix = iterables.sliceArray(0..index)
            val partial = CartesianProductGenerator.generate(*suffix)
            total += partial.totalSize
            partial
        }

        return CombinatorialSequence(
            total,
            sequence {
                partials.forEach { partial -> yieldAll(partial) }
            },
        )
    }

    /**
     * Returns a sequence of power set of the elements of [iterable].
     */
    @JvmStatic
    public fun <T> powerset(iterable: Iterable<T>): CombinatorialSequence<List<T>> =
        PowerSetGenerator.generate(iterable)

    /**
     * Returns a sequence of permutations of [length] of the elements of [array].
     *
     * If [length] is not specified or is null, the default for [length] is the length of [array].
     *
     * @throws IllegalArgumentException if [length] is negative.
     */
    public inline fun <reified T> permutations(array: Array<T>, length: Int? = null): CombinatorialSequence<Array<T>> =
        PermutationsGenerator.generate(array, length)

    /**
     * Returns a sequence of permutations with repetition of [length] of the elements of [array].
     *
     * @throws IllegalArgumentException if [length] is negative.
     */
    public inline fun <reified T> permutationsWithRepetition(array: Array<T>, length: Int): CombinatorialSequence<Array<T>> =
        PermutationsWithRepetitionGenerator.generate(array, length = length)

    /**
     * Returns a sequence of derangement of the elements of [array].
     */
    public inline fun <reified T> derangements(array: Array<T>): CombinatorialSequence<Array<T>> =
        DerangementGenerator.generate(array)

    /**
     * Returns a sequence of combinations of [length] of the elements of [array].
     *
     * @throws IllegalArgumentException if [length] is negative.
     */
    public inline fun <reified T> combinations(array: Array<T>, length: Int): CombinatorialSequence<Array<T>> =
        CombinationsGenerator.generate(array, length)

    /**
     * Returns a sequence of combinations with repetition of [length] of the elements of [array].
     *
     * @throws IllegalArgumentException if [length] is negative.
     */
    public inline fun <reified T> combinationsWithRepetition(array: Array<T>, length: Int): CombinatorialSequence<Array<T>> =
        CombinationsWithRepetitionGenerator.generate(array, length)

    /**
     * Returns a sequence of cartesian product of the elements of [arrays].
     *
     * To compute the cartesian product of [arrays] with itself,
     * specify the number of repetitions with the [repeat] named argument.
     *
     * @throws IllegalArgumentException if [repeat] is negative.
     */
    public inline fun <reified T> cartesianProduct(vararg arrays: Array<T>, repeat: Int = 1): CombinatorialSequence<Array<T>> =
        CartesianProductGenerator.generate(*arrays, repeat = repeat)

    /**
     * Returns a sequence of progressive cartesian products of the elements of [iterables].
     *
     * The combinations are generated progressively from **left to right**:
     * - First, the first iterable alone (length = 1)
     * - Then the first two iterables combined (length = 2)
     * - And so on until all iterables are combined
     *
     * Example:
     * ```
     * val list1 = listOf("A", "B")
     * val list2 = listOf("1", "2")
     * val list3 = listOf("x", "y")
     *
     * val result = Combinatorics.progressiveCartesian(list1, list2, list3)
     * result.forEach { println(it) }
     * ```
     * Output:
     * ```
     * [A]
     * [B]
     * [A, 1]
     * [A, 2]
     * [B, 1]
     * [B, 2]
     * [A, 1, x]
     * [A, 1, y]
     * [A, 2, x]
     * [A, 2, y]
     * [B, 1, x]
     * [B, 1, y]
     * [B, 2, x]
     * [B, 2, y]
     * ```
     *
     * @param iterables Vararg of iterables to combine progressively.
     * @return A [CombinatorialSequence] containing all progressive combinations.
     */
    @JvmStatic
    public inline fun <reified T> progressiveCartesianProduct(vararg arrays: Array<T>): CombinatorialSequence<Array<T>> {
        if (arrays.isEmpty())
            return CombinatorialSequence(BigInteger.ZERO, sequenceOf())

        // Compute total size eagerly using totalSize property
        var total: BigInteger = BigInteger.ZERO
        val partials = arrays.indices.map { index ->
            val suffix = arrays.sliceArray(0..index)
            val partial = CartesianProductGenerator.generate(*suffix)
            total += partial.totalSize
            partial
        }

        return CombinatorialSequence(
            total,
            sequence {
                partials.forEach { partial -> yieldAll(partial) }
            },
        )
    }

    /**
     * Returns a sequence of power set of the elements of [array].
     */
    public inline fun <reified T> powerset(array: Array<T>): CombinatorialSequence<Array<T>> =
        PowerSetGenerator.generate(array)
}
