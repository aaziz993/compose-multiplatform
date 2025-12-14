package klib.data

public inline fun <T : AutoCloseable?, R> T.nullableUse(block: (T) -> R?): R? = try {
    use(block)
}
catch (_: NullPointerException) {
    null
}
