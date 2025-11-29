package klib.data.type.collections.linkedlist

import klib.data.type.collections.linkedlist.model.DataNode

public class DoublyMutableIterator<T>(private val mutableList: MutableLinkedList<T>) : MutableIterator<DataNode<T>> {

    private var current: DataNode<T>? = null
    private var next: DataNode<T>? = mutableList.head

    override fun hasNext(): Boolean = next != null

    override fun next(): DataNode<T> {
        current = next ?: throw NoSuchElementException()
        next = if (next?.next == mutableList.head) null else next?.next
        return current!!
    }

    override fun remove() {
        mutableList.deleteNode(requireNotNull(current) { "Call next() before remove()" })
        current = null
    }
}
