/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package klib.data.type.collections.bimap

/**
 * A modifiable bimap. see [BiMap] to find read-only access methods.
 *
 * @param K the type of map keys.
 * @param V the type of map values.
 */
public interface MutableBiMap<K, V> : MutableMap<K, V>, BiMap<K, V> {

    override val inverse: MutableBiMap<V, K>

    override val values: MutableSet<V>

    /**
     * Associates the specified [value] with the specified [key] in the bimap.
     *
     * The bimap throws [IllegalArgumentException] if the given value is already
     * bound to a different key in it. The bimap will remain unmodified in this
     * event. To avoid this exception, call [forcePut] instead.
     *
     * @param key the key with which the specified value is to be associated.
     * @param value the value to be associated with the specified key.
     * @return the previous value associated with the key, or `null` if the key
     *         was not present in the bimap.
     */
    override fun put(key: K, value: V): V?

    /**
     * An alternate form of [put] that silently removes any existing entry
     * with the value [value] before proceeding with the [put] operation.
     *
     * If the bimap previously contained the provided key-value
     * mapping, this method has no effect.
     *
     * Note that a successful call to this method could cause the size of the
     * bimap to increase by one, stay the same, or even decrease by one.
     *
     * __Warning__: If an existing entry with this value is removed, the key
     * for that entry is discarded and not returned.
     *
     * @param key the key with which the specified value is to be associated.
     * @param value the value to be associated with the specified key.
     * @return the value which was previously associated with the key, or `null`
     *         if there was no previous entry.
     */
    public fun forcePut(key: K, value: V): V?
}

internal class MutableBiMapImpl<K, V> : MutableBiMap<K, V> {

    private val forward = mutableMapOf<K, V>()
    private val backward = mutableMapOf<V, K>()

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>> get() = forward.entries
    override val keys: MutableSet<K> get() = forward.keys
    override val values: MutableSet<V> get() = forward.values.toMutableSet()
    override val size: Int get() = forward.size

    override val inverse: MutableBiMap<V, K> by lazy {
        object : MutableBiMap<V, K> {
            override val inverse: MutableBiMap<K, V> get() = this@MutableBiMapImpl
            override val keys: MutableSet<V> get() = backward.keys
            override val values: MutableSet<K> get() = backward.values.toMutableSet()
            override val entries: MutableSet<MutableMap.MutableEntry<V, K>> get() = backward.entries
            override val size: Int get() = backward.size

            override fun put(key: V, value: K): K? {
                if (forward.containsKey(value) && forward[value] != key)
                    throw IllegalArgumentException("Value '$value' already bound to another key")
                return forcePut(key, value)
            }

            override fun forcePut(key: V, value: K): K? {
                val oldKey = backward.put(key, value)
                oldKey?.let(forward::remove)
                forward[value] = key
                return oldKey
            }

            override fun putAll(from: Map<out V, K>) = from.forEach(::put)
            override fun remove(key: V): K? = backward[key]?.also { value ->
                forward.remove(value)
                backward.remove(key)
            }

            override fun clear() = this@MutableBiMapImpl.clear()
            override fun containsKey(key: V) = backward.containsKey(key)
            override fun containsValue(value: K) = forward.containsKey(value)
            override fun get(key: V) = backward[key]
            override fun isEmpty() = backward.isEmpty()
        }
    }

    override fun put(key: K, value: V): V? {
        if (backward.containsKey(value) && backward[value] != key)
            throw IllegalArgumentException("Value '$value' already bound to another key")
        return forcePut(key, value)
    }

    override fun forcePut(key: K, value: V): V? {
        val oldValue = forward.put(key, value)
        oldValue?.let(backward::remove)
        backward[value] = key
        return oldValue
    }

    override fun putAll(from: Map<out K, V>) = from.forEach(::put)

    override fun remove(key: K): V? = forward[key]?.also { value ->
        backward.remove(value)
        forward.remove(key)
    }

    override fun clear() {
        forward.clear()
        backward.clear()
    }

    override fun containsKey(key: K) = forward.containsKey(key)
    override fun containsValue(value: V) = backward.containsKey(value)
    override fun get(key: K) = forward[key]
    override fun isEmpty() = forward.isEmpty()
}

/**
 * Returns a new mutable bimap with the specified contents, given as a list of pairs
 * where the first value is the key and the second is the value.
 *
 * If multiple pairs have the same key or the same value, the resulting bimap will contain
 * the last of those pairs.
 *
 * Entries of the bimap are iterated in the order they were specified.
 *
 * @param pairs the specified contents for the returned bimap
 * @return a new mutable bimap
 */
public fun <K, V> mutableBiMapOf(vararg pairs: Pair<K, V>): MutableBiMap<K, V> =
    MutableBiMapImpl<K, V>().apply { putAll(pairs) }

/**
 * Returns a new mutable bimap containing all key-value pairs from the given map.
 *
 * The returned bimap preserves the entry iteration order of the original map.
 *
 * @receiver the original map
 * @return a new mutable bimap
 */
public fun <K, V> Map<K, V>.toMutableBiMap(): MutableBiMap<K, V> =
    MutableBiMapImpl<K, V>().apply { putAll(this@toMutableBiMap) }
