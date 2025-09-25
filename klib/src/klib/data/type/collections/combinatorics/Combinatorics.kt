@file:JvmName("Combinatorics")

package klib.data.type.collections.combinatorics

import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

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
     * Returns a sequence of power set of the elements of [array].
     */
    public inline fun <reified T> powerset(array: Array<T>): CombinatorialSequence<Array<T>> =
        PowerSetGenerator.generate(array)
}
