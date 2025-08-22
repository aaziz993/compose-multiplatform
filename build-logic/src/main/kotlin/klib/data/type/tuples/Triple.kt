package klib.data.type.tuples

public infix fun <A, B, C, D> Triple<A, B, C>.and(that: D): Quadruple<A, B, C, D> =
    Quadruple(first, second, third, that)

public infix fun <A, B, C, D> A.and(that: Triple<B, C, D>): Quadruple<A, B, C, D> =
    Quadruple(this, that.first, that.second, that.third)
