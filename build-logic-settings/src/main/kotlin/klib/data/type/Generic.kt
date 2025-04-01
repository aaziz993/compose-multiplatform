package klib.data.type

public inline fun <T> T.act(action: () -> Unit): T {
    action()
    return this
}
