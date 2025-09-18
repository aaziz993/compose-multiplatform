package klib.data.cache

public interface Cache<K, V> {

    public operator fun get(key: K): V?

    public fun getOrPut(key: K, defaultValue: (key: K) -> V): V = get(key) ?: defaultValue(key).also { value ->
        set(key, value)
    }

    public operator fun set(key: K, value: V)

    public fun remove(key: K)

    public fun clear()

    public fun asMap(): Map<K, V>
}
