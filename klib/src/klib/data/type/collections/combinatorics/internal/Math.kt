@file:JvmName("Math")

package klib.data.type.collections.combinatorics.internal

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import kotlin.jvm.JvmName
import kotlin.math.max

@PublishedApi
internal fun permutations(n: Int, r: Int): BigInteger {
    require(n >= 0 && r >= 0 && n >= r)
    return permutationsHelper(n, r)
}

@PublishedApi
internal fun combinations(n: Int, r: Int): BigInteger {
    require(n >= 0 && r >= 0 && n >= r)
    return permutationsHelper(n, r) / factorialHelper(r)
}

@PublishedApi
internal fun combinationsWithRepetition(n: Int, r: Int): BigInteger {
    require(n >= 1 && r >= 0)
    return factorialHelper(n + r - 1) / factorialHelper(r) / factorialHelper(n - 1)
}

@PublishedApi
internal fun subFactorial(n: Int): BigInteger {
    require(n >= 0) { "n must be non-negative, was $n" }
    if (n == 0) return BigInteger.ONE
    var prev = BigInteger.ONE
    var acc = BigInteger.ZERO
    for (i in 2..n) {
        val next = (i - 1).toBigInteger() * (acc + prev)
        prev = acc
        acc = next
    }
    return acc
}

private fun factorialHelper(n: Int): BigInteger = n.toBigInteger().factorial()

private fun permutationsHelper(n: Int, r: Int): BigInteger {
    var acc = BigInteger.ONE
    for (i in max(2, n - r + 1)..n) {
        acc *= i.toBigInteger()
    }
    return acc
}
