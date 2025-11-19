package klib.data.type.collections.list.linked

public interface LinkedList<T> : Iterable<Node<T>> {

    public val head: Node<T>?
    public val tail: Node<T>?
    public val size: Int
}
