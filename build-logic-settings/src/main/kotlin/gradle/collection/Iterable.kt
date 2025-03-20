package gradle.collection

public inline fun <T> Iterable<T>.act(action: () -> Unit): Iterable<T> {
    action()
    return this
}

public inline fun <K, V : Any> Iterable<K>.associateWithNotNull(valueSelector: (K) -> V?): Map<K, V> {
    @Suppress("UNCHECKED_CAST")
    return associateWith { valueSelector(it) }.filterValues { it != null } as Map<K, V>
}
