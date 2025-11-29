package klib.data.type.collections.linkedlist

import klib.data.type.collections.linkedlist.model.DataNode

public interface LinkedList<T> : Iterable<DataNode<T>> {

    public val head: DataNode<T>?
    public val tail: DataNode<T>?
    public val size: Int
}
