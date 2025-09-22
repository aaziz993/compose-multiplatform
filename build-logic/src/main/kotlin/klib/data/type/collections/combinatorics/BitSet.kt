package klib.data.type.collections.combinatorics

public typealias BitSet = BooleanArray

public fun BitSet.set(bitIndex: Int) {
    require(bitIndex >= 0) { "bitIndex < 0: $bitIndex" }

    this[bitIndex] = true
}

public fun BitSet.set(fromIndex: Int = 0, toIndex: Int = size, value: Boolean = true) = set(fromIndex, toIndex) { value }

public fun BitSet.clear(bitIndex: Int) {
    require(bitIndex >= 0) { "bitIndex < 0: $bitIndex" }

    this[bitIndex] = false
}

public fun BitSet.clear(fromIndex: Int = 0, toIndex: Int = size) = set(fromIndex, toIndex, false)

public fun BitSet.flip(fromIndex: Int, toIndex: Int) = set(fromIndex, toIndex) { !it }

public fun BitSet.nextSetBit(fromIndex: Int): Int = nextBit(fromIndex) { it }

public fun BitSet.previousSetBit(fromIndex: Int): Int = previousBit(fromIndex) { it }

public fun BitSet.nextClearBit(fromIndex: Int): Int = nextBit(fromIndex) { !it }

public fun BitSet.previousClearBit(fromIndex: Int): Int = previousBit(fromIndex) { !it }

public fun BitSet.cardinality(): Int = count { it }

private fun BitSet.set(fromIndex: Int = 0, toIndex: Int = size, value: (Boolean) -> Boolean) {
    checkRange(fromIndex, toIndex)

    if (fromIndex == toIndex) return

    for (i in fromIndex until toIndex) {
        this[i] = value(this[i])
    }
}

private fun BitSet.nextBit(fromIndex: Int, predicate: (Boolean) -> Boolean): Int {
    for (i in fromIndex until size) {
        if (predicate(this[i])) {
            return i
        }
    }
    return -1
}

private fun BitSet.previousBit(fromIndex: Int, predicate: (Boolean) -> Boolean): Int {
    for (i in fromIndex downTo 0) {
        if (predicate(this[i])) {
            return i
        }
    }
    return -1
}

private fun checkRange(fromIndex: Int, toIndex: Int) {
    if (fromIndex < 0) throw IndexOutOfBoundsException("fromIndex < 0: $fromIndex")
    if (toIndex < 0) throw IndexOutOfBoundsException("toIndex < 0: $toIndex")
    if (fromIndex > toIndex) throw IndexOutOfBoundsException("fromIndex: $fromIndex  > toIndex: $toIndex")
}

