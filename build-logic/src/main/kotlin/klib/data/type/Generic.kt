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

