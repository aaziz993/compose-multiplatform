@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.collections.bimap

/**
 * A bimap (or "bidirectional map") is a map that preserves the uniqueness of
 * its values as well as that of its keys. This constraint enables bimaps to
 * support an "inverse view", which is another bimap containing the same entries
 * as this bimap but with reversed keys and values.
 *
 * @param K the type of map keys.
 * @param V the type of map values.
 * @see MutableBiMap
 */
public interface BiMap<K, V> : Map<K, V> {

    /**
     * Returns the inverse view of this bimap, which maps each of this bimap's
     * values to its associated key. The two bimaps are backed by the same data;
     * any changes to one will appear in the other.
     *
     * __Note:__ There is no guaranteed correspondence between the iteration
     * order of a bimap and that of its inverse.
     *
     * @return the inverse view of this bimap
     */
    public val inverse: BiMap<V, K>

    /**
     * Returns a [Set] view of the values contained in this bimap.
     *
     * The set is backed by the bimap, so changes to the bimap are
     * reflected in the set.
     *
     * @return a set view of the values contained in this bimap
     */
    override val values: Set<V>
}

private object EmptyBiMap : BiMap<Nothing, Nothing> {

    override fun equals(other: Any?): Boolean = other is Map<*, *> && other.isEmpty()
    override fun hashCode(): Int = 0
    override fun toString(): String = "{}"

    override val size: Int get() = 0
    override fun isEmpty(): Boolean = true

    override fun containsKey(key: Nothing): Boolean = false
    override fun containsValue(value: Nothing): Boolean = false
    override fun get(key: Nothing): Nothing? = null
    override val entries: Set<Map.Entry<Nothing, Nothing>> get() = EmptySet
    override val keys: Set<Nothing> get() = EmptySet
    override val values: Set<Nothing> = EmptySet
    override val inverse: BiMap<Nothing, Nothing> = this
}

/**
 * Returns an empty read-only bimap of specified type.
 *
 * @return an empty read-only bimap
 */
public fun <K, V> emptyBiMap(): BiMap<K, V> = @Suppress("UNCHECKED_CAST") (EmptyBiMap as BiMap<K, V>)

/**
 * Returns a new read-only bimap containing all key-value pairs from the given map.
 *
 * The returned bimap preserves the entry iteration order of the original map.
 *
 * @receiver the original map
 * @return a new read-only bimap
 */
public fun <K, V> Map<K, V>.toBiMap(): BiMap<K, V> =
    if (isNotEmpty()) {
        val inversePairs = entries.associate { (key, value) -> value to key }
        BiMapImpl(this, inversePairs)
    }
    else emptyBiMap()

/**
 * Returns a new read-only bimap with the specified contents, given as a list of pairs
 * where the first value is the key and the second is the value.
 *
 * If multiple pairs have the same key or the same value, the resulting bimap will contain
 * the last of those pairs.
 *
 * Entries of the bimap are iterated in the order they were specified.
 *
 * @param pairs the specified contents for the returned bimap
 * @return a new read-only bimap
 */
public fun <K, V> biMapOf(vararg pairs: Pair<K, V>): BiMap<K, V> = pairs.toMap().toBiMap()

/**
 * Returns a new read-only bimap, mapping only the specified key to the
 * specified value.
 *
 * @param pair a pair of key and value for the returned bimap
 * @return a new read-only bimap
 */
public fun <K, V> biMapOf(pair: Pair<K, V>): BiMap<K, V> =
    BiMapImpl(mapOf(pair), mapOf(pair.second to pair.first))

private class BiMapImpl<K, V> private constructor(delegate: Map<K, V>) :
    BiMap<K, V>, Map<K, V> by delegate {

    constructor(forward: Map<K, V>, backward: Map<V, K>) : this(forward) {
        inverse = BiMapImpl(backward, this)
    }

    private constructor(backward: Map<K, V>, forward: BiMap<V, K>) : this(backward) {
        inverse = forward
    }

    override lateinit var inverse: BiMap<V, K>
        private set

    override val values: Set<V> get() = inverse.keys

    override fun equals(other: Any?): Boolean = equals(this, other)

    override fun hashCode(): Int = hashCodeOf(this)
}

internal fun equals(bimap: BiMap<*, *>, other: Any?): Boolean {
    if (bimap === other) return true
    if (other !is BiMap<*, *>) return false
    if (other.size != bimap.size) return false
    val i = bimap.entries.iterator()
    while (i.hasNext()) {
        val e = i.next()
        val key = e.key
        val value = e.value
        if (value == null) {
            if (other[key] != null || !other.containsKey(key))
                return false
        }
        else {
            if (value != other[key])
                return false
        }
    }
    return true
}

internal fun hashCodeOf(map: Map<*, *>): Int {
    return map.entries.fold(0) { acc, entry ->
        acc + entry.hashCode()
    }
}
