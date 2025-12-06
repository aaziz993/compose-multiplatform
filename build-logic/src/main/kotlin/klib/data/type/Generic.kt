package klib.data.type

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

public inline fun <T, U> ifNonNull(t: T?, crossinline action: (T) -> U): U? =
    if (t != null) action(t) else null

