package klib.data.cache.kache.collection

import kotlin.jvm.JvmField

internal class MutableChainedScatterMap<K, V>(
    initialCapacity: Int = DefaultScatterCapacity,
    @JvmField internal val accessChain: MutableChain? = null,
    @JvmField internal val insertionChain: MutableChain? = null,
    @JvmField internal val accessOrder: Boolean = accessChain != null,
) : MutableScatterMap<K, V>(initialCapacity) {

    init {
        require(accessChain != null || insertionChain != null) { "At least, one chain must be not null" }

        accessChain?.initializeStorage(_capacity)
        insertionChain?.initializeStorage(_capacity)
    }

    @JvmField
    internal val mainChain: MutableChain = getChainByOrder(accessOrder)

    private var _keySet: ChainedKeys? = null
    override val keySet: Set<K> get() = _keySet ?: ChainedKeys(reversed = false).apply { _keySet = this }

    private var _reversedKeySet: ChainedKeys? = null
    internal val reversedKeySet: Set<K>
        get() = _reversedKeySet ?: ChainedKeys(reversed = true).apply { _reversedKeySet = this }

    override fun onAccess(index: Int) {
        accessChain?.moveToEnd(index)
    }

    override fun onInsertion(index: Int) {
        accessChain?.addToEnd(index)
        insertionChain?.addToEnd(index)
    }

    override fun onReplacement(index: Int) {
        accessChain?.moveToEnd(index)
        insertionChain?.moveToEnd(index)
    }

    override fun onRemoval(index: Int) {
        accessChain?.remove(index)
        insertionChain?.remove(index)
    }

    override fun clear() {
        super.clear()
        accessChain?.clear()
        insertionChain?.clear()
    }

    override fun resizeStorage(newCapacity: Int) {
        val previousKeys = keys
        val previousValues = values
        val accessoryChain = if (accessOrder) insertionChain else accessChain
        val newIndices = IntArray(_capacity)

        initializeStorage(newCapacity)

        val newMetadata = metadata
        val newKeys = keys
        val newValues = values
        val capacity = _capacity

        // Resize code copied from `MutableScatterMap`
        @Suppress("DuplicatedCode")
        mainChain.resizeStorage(_capacity) { i ->
            val previousKey = previousKeys[i]
            val hash = hash(previousKey)
            val index = findFirstAvailableSlot(h1(hash))

            writeMetadata(newMetadata, capacity, index, h2(hash).toLong())
            newKeys[index] = previousKey
            newValues[index] = previousValues[i]

            newIndices[i] = index
            index
        }

        accessoryChain?.resizeStorage(newCapacity) { newIndices[it] }
    }

    /**
     * Iterates over every key/value pair stored in this map by invoking the [block] function. The order of iteration
     * is defined by the [accessOrder] parameter. The order of iteration can be reversed by setting the [reversed]
     * parameter to `true`.
     */
    internal inline fun forEachIndexed(
        accessOrder: Boolean = this.accessOrder,
        reversed: Boolean = false,
        block: (key: K, value: V, index: Int) -> Unit,
    ) {
        getChainByOrder(accessOrder).forEachIndexed(reversed) { index ->
            @Suppress("UNCHECKED_CAST")
            block(keys[index] as K, values[index] as V, index)
        }
    }

    private fun getChainByOrder(accessOrder: Boolean): MutableChain =
        (if (accessOrder) accessChain else insertionChain)
            ?: throw IllegalStateException("The chain associated with the requested order is null")

    private inner class ChainedKeys(private val reversed: Boolean) : Set<K> {

        override val size: Int get() = _size

        override fun isEmpty(): Boolean = this@MutableChainedScatterMap.isEmpty()

        override fun iterator(): Iterator<K> = iterator {
            mainChain.forEachIndexed(reversed) { index -> @Suppress("UNCHECKED_CAST") yield(keys[index] as K) }
        }

        override fun containsAll(elements: Collection<K>): Boolean = elements.all { containsKey(it) }

        override fun contains(element: K): Boolean = containsKey(element)
    }
}
