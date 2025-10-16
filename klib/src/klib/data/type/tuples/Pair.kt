package klib.data.type.tuples

public fun <T> T.pair(): Pair<T, T> = this to this

public infix fun <A, B> A.to(second: (A) -> B): Pair<A, B> = this to second(this)

public infix fun <A, B, C> Pair<A, B>.and(t3: C): Tuple3<A, B, C> = Tuple3(first, second, t3)

public infix fun <A, B, C> A.and(that: Pair<B, C>): Tuple3<A, B, C> = Tuple3(this, that.first, that.second)
