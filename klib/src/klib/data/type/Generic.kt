package klib.data.type

import klib.data.type.tuples.Tuple4

public val <T> T.exhaustive: T
    get() = this

public fun <T> T.pair(): Pair<T, T> = this to this

public fun <T> T.triple(): Triple<T, T, T> = Triple(this, this, this)

public fun <T> T.quadruple(): Tuple4<T, T, T, T> = Tuple4(this, this, this, this)

public inline fun <T> T.act(block: () -> Unit): T {
    block()
    return this
}

public inline fun <T : Any> T?.alsoIfNull(block: () -> Unit): T? {
    if (this == null) block()
    return this
}

