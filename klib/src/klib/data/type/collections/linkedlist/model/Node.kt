package klib.data.type.collections.linkedlist.model

@Suppress("UNCHECKED_CAST")
public interface Node<T : Node<T>> : Iterable<T> {

    public val prev: T?
    public val next: T?

    public fun head(): T = prev?.head() ?: this as T

    public fun tail(): T = next?.tail() ?: this as T

    public fun prevIterator(): Iterator<T> = NodeIterator(this as T, Node<T>::prev)

    override fun iterator(): Iterator<T> = NodeIterator(this as T, Node<T>::next)
}

internal class NodeIterator<T : Node<T>>(private var node: T, private val next: Node<T>.() -> T?)
    : AbstractIterator<T>() {

    override fun computeNext() {
        node.next()?.let { next ->
            node = next
            setNext(node)
        } ?: done()
    }
}


