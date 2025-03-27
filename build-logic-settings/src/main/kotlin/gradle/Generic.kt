package gradle

public inline fun <T> T.act(action: () -> Unit): T {
    action()
    return this
}
