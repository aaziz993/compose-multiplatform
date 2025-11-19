package klib.data.type.collections.list.linked

public class DoublyMutableIterator<T>(private val mutableList: MutableLinkedList<T>) : MutableIterator<Node<T>> {

    private var current: Node<T>? = null
    private var next: Node<T>? = mutableList.head

    override fun hasNext(): Boolean = next != null

    override fun next(): Node<T> {
        current = next ?: throw NoSuchElementException()
        next = if (next?.next == mutableList.head) null else next?.next
        return current!!
    }

    override fun remove() {
        mutableList.deleteNode(requireNotNull(current) { "Call next() before remove()" })
        current = null
    }
}
