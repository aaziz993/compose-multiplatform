package klib.data.type.collections.combinatorics

import com.ionspin.kotlin.bignum.integer.BigInteger
import klib.data.type.collections.combinatorics.internal.combinations
import klib.data.type.collections.combinatorics.internal.combinationsWithRepetition
import klib.data.type.collections.combinatorics.internal.permutations

public infix fun Int.permutations(r: Int): BigInteger = permutations(this, r)

public infix fun Int.combinations(r: Int): BigInteger = combinations(this, r)

public infix fun Int.combinationsWithRepetition(r: Int): BigInteger = combinationsWithRepetition(this, r)

public fun <T> Iterable<T>.permutations(length: Int? = null): CombinatorialSequence<List<T>> = Combinatorics.permutations(this, length)

public fun <T> Iterable<T>.permutationsWithRepetition(length: Int): CombinatorialSequence<List<T>> = Combinatorics.permutationsWithRepetition(this, length)

public fun <T> Iterable<T>.derangements(): CombinatorialSequence<List<T>> = Combinatorics.derangements(this)

public fun <T> Iterable<T>.combinations(length: Int): CombinatorialSequence<List<T>> = Combinatorics.combinations(this, length)

public fun <T> Iterable<T>.combinationsWithRepetition(length: Int): CombinatorialSequence<List<T>> = Combinatorics.combinationsWithRepetition(this, length)

public fun <T> Iterable<T>.cartesianProduct(vararg others: Iterable<T>, repeat: Int = 1): CombinatorialSequence<List<T>> = Combinatorics.cartesianProduct(this, *others, repeat = repeat)

public fun <T> Iterable<T>.powerset(): CombinatorialSequence<List<T>> = Combinatorics.powerset(this)

public inline fun <reified T> Array<T>.permutations(length: Int? = null): CombinatorialSequence<Array<T>> = Combinatorics.permutations(this, length)

public inline fun <reified T> Array<T>.permutationsWithRepetition(length: Int): CombinatorialSequence<Array<T>> = Combinatorics.permutationsWithRepetition(this, length)

public inline fun <reified T> Array<T>.derangements(): CombinatorialSequence<Array<T>> = Combinatorics.derangements(this)

public inline fun <reified T> Array<T>.combinations(length: Int): CombinatorialSequence<Array<T>> = Combinatorics.combinations(this, length)

public inline fun <reified T> Array<T>.combinationsWithRepetition(length: Int): CombinatorialSequence<Array<T>> = Combinatorics.combinationsWithRepetition(this, length)

public inline fun <reified T> Array<T>.cartesianProduct(vararg others: Array<T>, repeat: Int = 1): CombinatorialSequence<Array<T>> = Combinatorics.cartesianProduct(this, *others, repeat = repeat)

public inline fun <reified T> Array<T>.powerset(): CombinatorialSequence<Array<T>> = Combinatorics.powerset(this)
