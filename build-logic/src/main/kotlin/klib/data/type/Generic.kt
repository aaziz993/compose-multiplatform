package klib.data.type

import klib.data.type.tuples.Tuple4

public val <T> T.exhaustive: T
    get() = this

public inline fun <T> T.act(block: () -> Unit): T {
    block()
    return this
}

public inline fun <T : Any> T?.alsoIfNull(block: () -> Unit): T? {
    if (this == null) block()
    return this
}

public inline fun <T : R, R> T.letIf(
    noinline condition: (T) -> Boolean,
    block: (T) -> R,
): R = if (condition(this)) {
    block(this)
}
else {
    this
}

public fun <T> T.pair(): Pair<T, T> = this to this

public infix fun <V, T> T.pairBy(other: V): Pair<V, T> = other to this

public fun <T> T.triple(): Triple<T, T, T> = Triple(this, this, this)

public fun <T> T.quadruple(): Tuple4<T, T, T, T> = Tuple4(this, this, this, this)


