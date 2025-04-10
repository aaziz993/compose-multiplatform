package klib.data.type

@JvmName("exceptionToNullExplicit")
public inline fun <T> exceptionToNull(block: () -> T): T? {
    return try {
        block()
    } catch (_: Throwable) {
        null
    }
}

@JvmName("exceptionToNullImplicit")
public fun <T> (() -> T).exceptionToNull(): T? = exceptionToNull(this)