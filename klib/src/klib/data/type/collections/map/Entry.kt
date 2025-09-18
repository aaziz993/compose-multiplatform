package klib.data.type.collections.map

public data class Entry<K, V>(
    override val key: K,
    override val value: V
) : Map.Entry<K, V>

public infix fun <K, V> K.with(that: V): Map.Entry<K, V> = Entry(this, that)

public infix fun <F, S> F.with(value: (F) -> S): Map.Entry<F, S> = this with value(this)
