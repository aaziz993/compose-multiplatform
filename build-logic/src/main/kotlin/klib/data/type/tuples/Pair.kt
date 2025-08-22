package klib.data.type.tuples

public fun <A> A.to(): Pair<A, A> = this to this

public infix fun <A, B> A.to(second: (A) -> B): Pair<A, B> = this to second(this)

public infix fun <A, B, C> Pair<A, B>.and(that: C): Triple<A, B, C> = Triple(first, second, that)

public infix fun <A, B, C> A.and(that: Pair<B, C>): Triple<A, B, C> =
    Triple(this, that.first, that.second)
