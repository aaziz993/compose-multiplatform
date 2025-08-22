package klib.data.type

public inline fun <T> T.act(block: () -> Unit): T {
    block()
    return this
}

public inline fun <T : Any> T?.alsoIfNull(block: () -> Unit): T? {
    if (this == null) block()
    return this
}

